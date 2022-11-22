package com.csdn.biz.web.client.rest;

import com.csdn.biz.api.model.User;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Bean Validation 校验 {@link ClientHttpRequestInterceptor} 实现
 *
 * @Author: xiewenfeng
 * @Date: 2022/11/18 13:38
 */
public class ValidatingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {

    public final static String VALIDATION_HEADER_NAME = "validation-result";

    private final Validator validator;

    private final HttpMessageConverter[] converters;

    public ValidatingClientHttpRequestInterceptor(Validator validator,
                                                  HttpMessageConverter... converters) {
        this.validator = validator;
        this.converters = converters;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = null;
        // 前置处理
        boolean valid = beforeExecute(request, body);
        HttpHeaders headers = request.getHeaders();
        headers.add(VALIDATION_HEADER_NAME, Boolean.toString(valid));
        // 请求处理
        response = execution.execute(request, body);
        // 后置处理
        return afterExecute(response);

    }

    private ClientHttpResponse afterExecute(ClientHttpResponse response) {
        return response;
    }

    private boolean beforeExecute(HttpRequest request, byte[] body) {
        return validateBean(request, body);
    }

    private boolean validateBean(HttpRequest request, byte[] body) {
        Class<?> bodyClass = resolveBodyClass(request.getHeaders());
        if (null == bodyClass) {
            return false;
        }
        HttpInputMessage inputMessage = new MappingJacksonInputMessage(new ByteArrayInputStream(body),
                request.getHeaders());
        MediaType mediaType = resolveMediaType(request.getHeaders());
        for (HttpMessageConverter converter : converters) {
            if (converter.canRead(bodyClass, mediaType)) {
                try {
                    Object bean = converter.read(bodyClass, inputMessage);
                    Set<ConstraintViolation<Object>> validates = validator.validate(bean);
                    if (!CollectionUtils.isEmpty(validates)) {
                        return false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    private MediaType resolveMediaType(HttpHeaders headers) {
        return headers.getContentType();
    }

    private Class<?> resolveBodyClass(HttpHeaders headers) {
        List<String> bodyClasses = headers.remove("body-class");
        if (CollectionUtils.isEmpty(bodyClasses)) {
            return User.class;
        }
        String bodyClassName = bodyClasses.get(0);
        return StringUtils.hasText(bodyClassName) ? ClassUtils.resolveClassName(bodyClassName, null)
                : User.class;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

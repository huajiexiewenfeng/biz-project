package com.csdn.biz.web.client.rest;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static com.csdn.biz.web.client.rest.ValidatingClientHttpRequestInterceptor.VALIDATION_HEADER_NAME;

/**
 * 请求前置错误处理
 *
 * @author ：xwf
 * @date ：Created in 2022\11\19 0019 19:17
 */
public class ErrorClientHttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        String valid = headers.getFirst(VALIDATION_HEADER_NAME);
        if (!"true".equals(valid)) {
            // TODO 异常处理
        }
        return execution.execute(request, body);
    }

    /**
     * 最低优先级
     *
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

package com.csdn.biz.web.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.servlet.function.ServerResponse.async;

/**
 * @Author: xiewenfeng
 * @Date: 2022/11/18 15:51
 */
@Configuration(proxyBeanMethods = false)
@Import(ErrorClientHttpRequestInterceptor.class)
public class RestTemplateConfiguration {

    @Bean
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor(Validator validator,
                                                                     MappingJackson2HttpMessageConverter httpMessageConverter) {
        return new ValidatingClientHttpRequestInterceptor(validator, httpMessageConverter);
    }

    /**
     * 没有其他参数的情况下可以采用 import 的方式引入
     *
     * @return
     */
//    @Bean
//    public ClientHttpRequestInterceptor errorClientHttpRequestInterceptor() {
//        return new ErrorClientHttpRequestInterceptor();
//    }

    @Bean
    @Primary
    public RestTemplate restTemplate(List<ClientHttpRequestInterceptor> interceptors,
                                     MappingJackson2HttpMessageConverter httpMessageConverter) {
        // 排序 ClientHttpRequestInterceptor 按顺序执行
        AnnotationAwareOrderComparator.sort(interceptors);
        List<HttpMessageConverter<?>> messageConverters = Arrays.asList(httpMessageConverter);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);
        ClientHttpRequestFactory requestFactory = buildClientHttpRequestFactory(interceptors);
        restTemplate.setRequestFactory(requestFactory);
        // TODO 增加 ResponseErrorHandler
        async(()->{

        });
        return restTemplate;
    }

    @Bean
    public MappingJackson2HttpMessageConverter httpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter
                .setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        return mappingJackson2HttpMessageConverter;
    }

    private ClientHttpRequestFactory buildClientHttpRequestFactory(
            List<ClientHttpRequestInterceptor> interceptors) {
        // TODO 替换 SimpleClientHttpRequestFactory
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        return new InterceptingClientHttpRequestFactory(requestFactory, interceptors);
    }
}

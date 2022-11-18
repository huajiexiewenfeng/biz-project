package com.csdn.biz.web.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: xiewenfeng
 * @Date: 2022/11/18 15:51
 */
@Configuration(proxyBeanMethods = false)
public class RestTemplateConfiguration {

  @Bean
  public ClientHttpRequestInterceptor clientHttpRequestInterceptor(Validator validator,
      MappingJackson2HttpMessageConverter httpMessageConverter) {
    return new ValidatingClientHttpRequestInterceptor(validator, httpMessageConverter);
  }

  @Bean
  @Primary
  public RestTemplate restTemplate(List<ClientHttpRequestInterceptor> interceptors,
      MappingJackson2HttpMessageConverter httpMessageConverter) {
    List<HttpMessageConverter<?>> messageConverters = Arrays.asList(httpMessageConverter);
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setMessageConverters(messageConverters);
    ClientHttpRequestFactory requestFactory = buildClientHttpRequestFactory(interceptors);
    restTemplate.setRequestFactory(requestFactory);
    // TODO 增加 ResponseErrorHandler
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

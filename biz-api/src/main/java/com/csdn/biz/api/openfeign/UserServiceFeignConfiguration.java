package com.csdn.biz.api.openfeign;

import com.csdn.biz.api.fault.tolerance.openfeign.BulkheadRequestInterceptor;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author ：xwf
 * @date ：Created in 2022\11\22 0022 16:43
 */
@Configuration(proxyBeanMethods = false)
public class UserServiceFeignConfiguration {

    /**
     * 对象创建于 Spring Boot
     * {@link HttpMessageConvertersAutoConfiguration#messageConverters(ObjectProvider)}
     */
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    @Primary
    public Decoder decoder() {
        return new ApiResponseDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters)));
    }

    @Bean
    public BulkheadRequestInterceptor bulkheadRequestInterceptor(){
        return new BulkheadRequestInterceptor();
    }

}

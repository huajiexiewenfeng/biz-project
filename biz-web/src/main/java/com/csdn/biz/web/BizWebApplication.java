package com.csdn.biz.web;

import com.csdn.biz.web.servlet.mvc.interceptor.ResourceBulkheadHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
@ServletComponentScan
@Import(value = {
        ResourceBulkheadHandlerInterceptor.class
})
public class BizWebApplication implements WebMvcConfigurer {

  @Autowired
  private List<HandlerInterceptor> handlerInterceptors;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    handlerInterceptors.forEach(registry::addInterceptor);
  }


  public static void main(String[] args) {
    SpringApplication.run(BizWebApplication.class, args);
  }

}

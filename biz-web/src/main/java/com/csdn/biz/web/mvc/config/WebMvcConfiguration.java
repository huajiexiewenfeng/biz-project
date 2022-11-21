package com.csdn.biz.web.mvc.config;

import com.csdn.biz.web.mvc.method.annotation.ApiResponseHandlerMethodReturnValueHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * Web MVC 配置类
 *
 * @Author: xiewenfeng
 * @Date: 2022/11/17 15:06
 */
@Configuration
public class WebMvcConfiguration {

  @Autowired
  public void resetRequestMappingHandlerAdapter(
      RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
    List<HandlerMethodReturnValueHandler> oldReturnValueHandlers = requestMappingHandlerAdapter
        .getReturnValueHandlers();
    List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>(
        oldReturnValueHandlers);
    newReturnValueHandlers.add(0, new ApiResponseHandlerMethodReturnValueHandler());
    requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
  }

  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }

}

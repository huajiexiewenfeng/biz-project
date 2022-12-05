package com.csdn.biz.web.servlet.mvc.method.annotation;

import com.csdn.biz.api.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author: xiewenfeng
 * @Date: 2022/12/5 20:23
 */
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<ApiResponse> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    @Override
    public ApiResponse beforeBodyWrite(ApiResponse body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return null;
    }
}

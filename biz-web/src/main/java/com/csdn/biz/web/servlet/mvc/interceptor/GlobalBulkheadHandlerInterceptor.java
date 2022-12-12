package com.csdn.biz.web.servlet.mvc.interceptor;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadConfig;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局 Spring Web MVC 限流
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/10 19:04
 * @see io.github.resilience4j.bulkhead.internal.SemaphoreBulkhead
 */
public class GlobalBulkheadHandlerInterceptor implements HandlerInterceptor, InitializingBean, DisposableBean {

    private Bulkhead bulkhead;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否需要限流
        // 正常执行 postHandle 方法
        // 正常执行 afterCompletion 方法
        bulkhead.acquirePermission();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 记录
        Bulkhead.EventPublisher eventPublisher = bulkhead.getEventPublisher();


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除状态
        bulkhead.releasePermission();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BulkheadConfig config = BulkheadConfig.custom().build();
        bulkhead = Bulkhead.of("globalBulkheadHandlerInterceptor", config);
    }

    @Override
    public void destroy() throws Exception {

    }

}

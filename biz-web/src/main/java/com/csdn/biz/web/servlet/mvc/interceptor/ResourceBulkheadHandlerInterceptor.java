package com.csdn.biz.web.servlet.mvc.interceptor;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源 Spring Web MVC 限流
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/10 19:04
 * @see io.github.resilience4j.bulkhead.internal.SemaphoreBulkhead
 */
public class ResourceBulkheadHandlerInterceptor implements HandlerInterceptor, InitializingBean, DisposableBean, ApplicationListener<ContextRefreshedEvent> {

    @Deprecated
    private Map<String, Bulkhead> bulkheadsCache;

    private Map<Method, Bulkhead> methodBulkheadsCache;

    private BulkheadConfig config;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否需要限流
        // 正常执行 postHandle 方法
        // 正常执行 afterCompletion 方法
        Bulkhead bulkhead = doGetBulkhead(handler);
        if (null != bulkhead) {
            bulkhead.acquirePermission();
        }
        return true;
    }

    private Bulkhead doGetBulkhead(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            return methodBulkheadsCache.get(method);
        }
        return null;
    }

    @Deprecated
    private String getResourceName(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AnnotationAttributes mergedAnnotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(method, RequestMapping.class);
            if (null != mergedAnnotationAttributes) {
                String[] paths = mergedAnnotationAttributes.getStringArray("path");
                return String.join("", paths);
            }
        }
        return "defaultURI";
    }

    @Deprecated
    private Bulkhead getBulkhead(Object handler) {
        String resourceName = getResourceName(handler);
        return bulkheadsCache.computeIfAbsent(resourceName, n -> Bulkhead.of(resourceName, config));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
        // 记录
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception {
        // 清除状态
        Bulkhead bulkhead = doGetBulkhead(handler);
        if (null != bulkhead) {
            bulkhead.releasePermission();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        config = BulkheadConfig.custom().build();
        bulkheadsCache = new ConcurrentHashMap<>();
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        methodBulkheadsCache = new HashMap<>();
        ApplicationContext context = event.getApplicationContext();
        Map<String, RequestMappingHandlerMapping> requestMappingHandlerMappingMap = context.getBeansOfType(RequestMappingHandlerMapping.class);
        for (RequestMappingHandlerMapping requestMappingHandlerMapping : requestMappingHandlerMappingMap.values()) {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
                HandlerMethod handlerMethod = entry.getValue();
                Method method = handlerMethod.getMethod();
                RequestMappingInfo requestMappingInfo = entry.getKey();
                String resourceName = requestMappingInfo.toString();
                methodBulkheadsCache.put(method, Bulkhead.of(resourceName, config));
            }
        }
    }
}

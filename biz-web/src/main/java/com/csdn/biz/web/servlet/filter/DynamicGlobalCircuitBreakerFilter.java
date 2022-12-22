package com.csdn.biz.web.servlet.filter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Set;

/**
 * 全局 {@link CircuitBreaker} Filter（粗粒度）
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/9 9:58
 */
@WebFilter(filterName = "dynamicGlobalCircuitBreakerWebFilter", urlPatterns = "/*", dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.INCLUDE,
        DispatcherType.FORWARD
})
@Component
public class DynamicGlobalCircuitBreakerFilter implements Filter, ApplicationContextAware {

    private CircuitBreaker circuitBreaker;
    private ApplicationContext applicationContext;
    private static final String FAILURE_RATE_THRESHOLD_KEY = "resilience4j.circuitbreaker.config.default.failureratethreshold";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.ofDefaults();
        // Create a CircuitBreakerRegistry with a custom global configuration
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);
        String filterName = filterConfig.getFilterName();
        // Get or create a CircuitBreaker from the CircuitBreakerRegistry
        // with the global default configuration
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("CircuitBreaker-" + filterName);
    }

    @EventListener(EnvironmentChangeEvent.class)
    public void onEnvironmentChangeEvent(EnvironmentChangeEvent event) {
        String name = circuitBreaker.getName();
        Set<String> keys = event.getKeys();
        if (keys.contains(FAILURE_RATE_THRESHOLD_KEY)) {
            Integer threshold = Integer.valueOf(applicationContext.getEnvironment().getProperty(FAILURE_RATE_THRESHOLD_KEY));
            CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().failureRateThreshold(threshold).build();
            CircuitBreakerRegistry circuitBreakerRegistry =
                    CircuitBreakerRegistry.of(circuitBreakerConfig);
            this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(name);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // method1()
        // 方法二
        try {
            circuitBreaker.decorateCheckedRunnable(() -> {
                chain.doFilter(request, response);
            }).run();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * 方法一
     *
     * @param request
     * @param response
     * @param chain
     */
    public void method1(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        circuitBreaker.acquirePermission();
        final long start = circuitBreaker.getCurrentTimestamp();
        try {
            chain.doFilter(request, response);
            long duration = circuitBreaker.getCurrentTimestamp() - start;
            circuitBreaker.onSuccess(duration, circuitBreaker.getTimestampUnit());
        } catch (Exception exception) {
            // Do not handle java.lang.Error
            long duration = circuitBreaker.getCurrentTimestamp() - start;
            circuitBreaker.onError(duration, circuitBreaker.getTimestampUnit(), exception);
            throw exception;
        }
    }


    @Override
    public void destroy() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

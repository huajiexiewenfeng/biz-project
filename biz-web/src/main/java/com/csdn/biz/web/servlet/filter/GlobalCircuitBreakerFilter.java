package com.csdn.biz.web.servlet.filter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 全局 {@link CircuitBreaker} Filter（粗粒度）
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/9 9:58
 */
@WebFilter(filterName = "globalCircuitBreakerFilter", urlPatterns = "/*", dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.INCLUDE,
        DispatcherType.FORWARD
})
public class GlobalCircuitBreakerFilter implements Filter {

    private CircuitBreaker circuitBreaker;

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
}

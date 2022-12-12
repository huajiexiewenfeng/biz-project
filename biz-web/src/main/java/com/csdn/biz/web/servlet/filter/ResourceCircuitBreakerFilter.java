package com.csdn.biz.web.servlet.filter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.apache.catalina.core.ApplicationFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 资源 {@link CircuitBreaker} Filter（细粒度）
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/9 9:58
 */
@WebFilter(filterName = "resourceCircuitBreakerFilter", urlPatterns = "/*", dispatcherTypes = {
        DispatcherType.REQUEST
})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResourceCircuitBreakerFilter implements Filter {

    /**
     * org.apache.catalina.core.ApplicationFilterFactory#createFilterChain(javax.servlet.ServletRequest, org.apache.catalina.Wrapper, javax.servlet.Servlet)
     */
    private final static String FILTER_CHAIN_IMPL_CLASS_NAME = "org.apache.catalina.core.ApplicationFilterChain";

    private final static Class<?> FILTER_CHAIN_IMPL_CLASS = ClassUtils.resolveClassName(FILTER_CHAIN_IMPL_CLASS_NAME, null);

    private CircuitBreakerRegistry circuitBreakerRegistry;

    private Map<String, CircuitBreaker> circuitBreakerCache;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.ofDefaults();
        // Create a CircuitBreakerRegistry with a custom global configuration
        this.circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);
        this.circuitBreakerCache = new ConcurrentHashMap<>();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String servletName = this.getServerName(chain);
        if (servletName == null) {
            chain.doFilter(request, response);
            return;
        }

        CircuitBreaker circuitBreaker = circuitBreakerCache.computeIfAbsent(servletName, circuitBreakerRegistry::circuitBreaker);

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

    private String getServerName(FilterChain chain) {
        if (FILTER_CHAIN_IMPL_CLASS != null) {
            Field field = ReflectionUtils.findField(FILTER_CHAIN_IMPL_CLASS, "servlet");
            field.setAccessible(true);
            final Servlet servlet = (Servlet) ReflectionUtils.getField(field, chain);
            if (null != servlet) {
                return servlet.getServletConfig().getServletName();
            }
        }
        return null;
    }

    @Override
    public void destroy() {

    }
}

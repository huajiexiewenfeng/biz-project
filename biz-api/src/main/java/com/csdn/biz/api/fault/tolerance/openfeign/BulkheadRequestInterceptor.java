package com.csdn.biz.api.fault.tolerance.openfeign;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 无法实现完整的拦截，没有前后
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/12 10:15
 */
@Deprecated
public class BulkheadRequestInterceptor implements RequestInterceptor, InitializingBean, DisposableBean {

    private Map<String, Bulkhead> bulkheadsCache;

    private BulkheadConfig config;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Request request = requestTemplate.request();
        String resourceName = request.httpMethod() + ":" + requestTemplate.path();
        Bulkhead bulkhead = getBulkhead(resourceName);
        if (null != bulkhead) {
            bulkhead.acquirePermission();
        }
    }

    private Bulkhead getBulkhead(String resourceName) {
        return bulkheadsCache.computeIfAbsent(resourceName, n -> Bulkhead.of(resourceName, config));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        config = BulkheadConfig.custom().build();
        bulkheadsCache = new ConcurrentHashMap<>();
    }

    @Override
    public void destroy() throws Exception {
        // 清除状态
        bulkheadsCache.forEach((name, bulkhead) -> {
            bulkhead.releasePermission();
        });
    }

}

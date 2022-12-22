package com.csdn.biz.data.fault.tolerance.mybatis;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.util.Properties;

/**
 * MyBatis {@link Interceptor}  resilience4j 实现
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/20 14:12
 */
public class Resilience4jMyBatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 如果当前 Interceptor 采用 Interceptor#plugin 默认实现
        // 即调用 Plugin.wrap(target, this); 当前方法才会被执行
        // 如果当前 Interceptor plugin 方法实现采用静态拦截(Wrapper)的方式，那么本方法不会被执行
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return decorateExecutor((Executor)target);
        }
        return Interceptor.super.plugin(target);
    }

    private Executor decorateExecutor(Executor target) {
        return new CircuitBreakerExecutorDecorator(target);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

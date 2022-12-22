package com.csdn.biz.data.fault.tolerance.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @Author: xiewenfeng
 * @Date: 2022/12/20 16:13
 */
public class CircuitBreakerExecutorDecorator extends ExecutorDecorator {

    public CircuitBreakerExecutorDecorator(Executor delegate) {
        super(delegate);
    }

    @Override
    protected void before(MappedStatement ms) {

    }

    @Override
    protected void after(MappedStatement ms) {

    }

    private String getResourceName(MappedStatement ms) {
        return ms.getId();
    }
}

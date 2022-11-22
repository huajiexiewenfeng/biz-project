package com.csdn.biz.client.web.loadbalancer;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

/**
 * @author ：xwf
 * @date ：Created in 2022\11\22 0022 12:28
 */
public class UserServiceServiceInstanceListSupplier implements ServiceInstanceListSupplier {
    @Override
    public String getServiceId() {
        return "user-service";
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(Arrays.asList(getLocalServiceInstance()));
    }

    private ServiceInstance getLocalServiceInstance() {
        DefaultServiceInstance instance = new DefaultServiceInstance();
        instance.setInstanceId("user-service");
        instance.setHost("127.0.0.1");
        instance.setPort(8080);
        return instance;
    }
}

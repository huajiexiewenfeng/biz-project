package com.csdn.biz.client.web.cloud.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * cpu 利用率负载均衡实现
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/24 22:20
 */
public class CpuUsageLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public CpuUsageLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier serviceInstanceListSupplier = serviceInstanceListSupplierProvider.getIfAvailable();
        Flux<List<ServiceInstance>> listFlux = serviceInstanceListSupplier.get();
        List<ServiceInstance> serviceInstances = listFlux.blockFirst();
        for (ServiceInstance serviceInstance : serviceInstances) {
            Map<String, String> metadata = serviceInstance.getMetadata();
            Integer cpuUsage = Integer.valueOf(metadata.get("cpu-usage"));
            // TODO CPU 利用率算法实现
        }
        return Mono.justOrEmpty(new DefaultResponse(serviceInstances.get(0)));
    }

}

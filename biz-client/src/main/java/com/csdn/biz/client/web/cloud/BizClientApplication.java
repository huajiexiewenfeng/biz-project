package com.csdn.biz.client.web.cloud;

import com.csdn.biz.api.interfaces.UserRegistrationService;
import com.csdn.biz.client.web.cloud.controller.BizClientFeignController;
import com.csdn.biz.client.web.cloud.loadbalancer.CpuUsageLoadBalancerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author: xiewenfeng
 * @Date: 2022/12/23 18:56
 */
@ComponentScan
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableFeignClients(clients = UserRegistrationService.class)
@LoadBalancerClient(name = "user-service", configuration = CpuUsageLoadBalancerConfiguration.class)
@EnableScheduling
public class BizClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizClientApplication.class, args);
    }

    @Autowired
    private BizClientFeignController bizClientFeignController;

    @Scheduled(fixedRate = 10 * 1000L)
    public void registerUser() {
        bizClientFeignController.registerUser();
    }

}

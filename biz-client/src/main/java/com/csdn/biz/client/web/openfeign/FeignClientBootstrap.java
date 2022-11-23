package com.csdn.biz.client.web.openfeign;

import com.csdn.biz.api.interfaces.UserRegistrationService;
import com.csdn.biz.api.model.User;
import com.csdn.biz.client.web.loadbalancer.LoadBalancerConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author liy：xwf
 * @date ：Created in 2022\11\21 0021 21:29
 */
@EnableFeignClients(clients = {UserRegistrationService.class})
@EnableAutoConfiguration
@LoadBalancerClient(name = "user-service", configuration = LoadBalancerConfiguration.class)
public class FeignClientBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(FeignClientBootstrap.class).build().run(args);
        UserRegistrationService bean = context.getBean(UserRegistrationService.class);
        Boolean result = bean.register(new User(1L, "123"));
        System.out.println(result);

    }
}

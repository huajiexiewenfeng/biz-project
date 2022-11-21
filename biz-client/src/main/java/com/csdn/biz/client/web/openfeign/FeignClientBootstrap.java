package com.csdn.biz.client.web.openfeign;

import com.csdn.biz.api.interfaces.UserRegistrationRestService;
import com.csdn.biz.api.interfaces.UserRegistrationService;
import com.csdn.biz.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：xwf
 * @date ：Created in 2022\11\21 0021 21:29
 */
@EnableFeignClients(clients = {UserRegistrationService.class})
@EnableAutoConfiguration
public class FeignClientBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(FeignClientBootstrap.class).build().run(args);
        UserRegistrationService bean = context.getBean(UserRegistrationService.class);
        Object result = bean.register(new User(1L, "123"));
        System.out.println(result);

    }
}

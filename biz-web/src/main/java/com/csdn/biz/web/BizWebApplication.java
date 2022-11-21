package com.csdn.biz.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BizWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(BizWebApplication.class, args);
  }

}

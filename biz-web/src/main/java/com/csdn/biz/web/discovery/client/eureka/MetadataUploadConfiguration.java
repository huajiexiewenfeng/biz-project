package com.csdn.biz.web.discovery.client.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

/**
 * @Author: xiewenfeng
 * @Date: 2022/12/23 21:50
 */
@ConditionalOnClass(name = {"org.springframework.cloud.client.discovery.DiscoveryClient",
        "org.springframework.cloud.client.serviceregistry.ServiceRegistry"})
@Configuration(proxyBeanMethods = false)
public class MetadataUploadConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MetadataUploadConfiguration.class);

    @Autowired
    private ServiceRegistry serviceRegistry;

    /**
     * 当前服务实例注册对象
     */
    @Autowired
    private Registration registration;

    /**
     * 在 Eureka 不生效
     */
    @Scheduled(fixedRate = 5000L, initialDelay = 30L)
    public void upload() {
        Map<String, String> metadata = registration.getMetadata();
        metadata.put("timestamp", String.valueOf(System.currentTimeMillis()));
        serviceRegistry.register(registration);
        logger.info("Upload Registration's metadata");
    }
}

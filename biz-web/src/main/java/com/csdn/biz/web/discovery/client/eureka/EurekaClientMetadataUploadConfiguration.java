package com.csdn.biz.web.discovery.client.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * Eureka Client 配置类
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/23 20:30
 */
@ConditionalOnClass(name = "com.netflix.discovery.DiscoveryClient")
@Configuration(proxyBeanMethods = false)
public class EurekaClientMetadataUploadConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(EurekaClientMetadataUploadConfiguration.class);

    private static final Boolean HOTSPOT_JVM = ClassUtils.isPresent("com.sun.management.OperatingSystemMXBean", null);

    @Autowired
    private EurekaClient eurekaClient;

    /**
     * 当前服务实例注册对象
     */
    @Autowired
    private Registration registration;

    @Scheduled(fixedRate = 5000L, initialDelay = 30L)
    public void upload() {
        String instanceId = registration.getInstanceId();
        Application application = eurekaClient.getApplication(registration.getServiceId());
        if (null == application) {
            return;
        }
        InstanceInfo instanceInfo = application.getByInstanceId(instanceId);
        Map<String, String> metadata = instanceInfo.getMetadata();
        metadata.put("timestamp", String.valueOf(System.currentTimeMillis()));
        metadata.put("cpu-usage", String.valueOf(getCpuUsage()));
        eurekaClient.getApplicationInfoManager().registerAppMetadata(metadata);
        logger.info("Upload Eureka Instance's metadata");
    }


    private Integer getCpuUsage() {
        if (HOTSPOT_JVM) {
            OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            Double usage = operatingSystemMXBean.getProcessCpuLoad() * 100;
            return usage.intValue();
        }
        return 0;
    }
}

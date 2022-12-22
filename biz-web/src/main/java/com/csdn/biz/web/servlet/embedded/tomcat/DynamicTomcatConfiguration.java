package com.csdn.biz.web.servlet.embedded.tomcat;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * @Author: xiewenfeng
 * @Date: 2022/12/22 19:10
 */
@Configuration
public class DynamicTomcatConfiguration implements TomcatProtocolHandlerCustomizer {

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private volatile ServerProperties originalServerProperties;

    @Autowired
    private Environment environment;

    private AbstractProtocol protocol;

    private Binder binder;

    @PostConstruct
    public void initBinder() {
        Iterable<ConfigurationPropertySource> configurationPropertySources = ConfigurationPropertySources.get(environment);
        binder = new Binder(configurationPropertySources);
        bindOriginalServerProperties();
    }

    private void buildOriginalServerProperties() {
        ServerProperties newServerProperties = new ServerProperties();
        BeanUtils.copyProperties(serverProperties, newServerProperties);
        this.originalServerProperties = newServerProperties;
    }

    private void bindOriginalServerProperties() {
        BindResult<ServerProperties> result = binder.bind("server", ServerProperties.class);
        ServerProperties newServerProperties = result.get();
        this.originalServerProperties = newServerProperties;
    }

    /**
     * 无法匹配驼峰
     *
     * @param event
     */
    @EventListener(EnvironmentChangeEvent.class)
    @Deprecated
    public void onEnvironmentChangeEvent(EnvironmentChangeEvent event) {
        Set<String> keys = event.getKeys();
        if (keys.contains("server.tomcat.threads.max")) {
            setMaxThreads();
        }

    }

    /**
     * 需要实时更新 originalServerProperties
     *
     * @param event
     */
    @EventListener(EnvironmentChangeEvent.class)
    public void onEnvironmentChangeEvent2(EnvironmentChangeEvent event) {
        ServerProperties.Tomcat.Threads threads = serverProperties.getTomcat().getThreads();
        ServerProperties.Tomcat.Threads originalThreads = originalServerProperties.getTomcat().getThreads();
        if (originalThreads.getMinSpare() != threads.getMinSpare()) {
            setMinSpareThreads();
        }

    }

    private void setMinSpareThreads() {
        int max = serverProperties.getTomcat().getThreads().getMinSpare();
        protocol.setMinSpareThreads(max);
        bindOriginalServerProperties();
    }

    private void setMaxThreads() {
        int max = serverProperties.getTomcat().getThreads().getMax();
        protocol.setMaxThreads(max);
    }


    @Override
    public void customize(ProtocolHandler protocolHandler) {
        if (protocolHandler instanceof AbstractProtocol) {
            protocol = (AbstractProtocol) protocolHandler;
        }
    }
}

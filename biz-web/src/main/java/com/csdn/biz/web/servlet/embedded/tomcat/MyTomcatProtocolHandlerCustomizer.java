package com.csdn.biz.web.servlet.embedded.tomcat;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

/**
 * Tomcat {@link ProtocolHandler} 自定义器
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/8 10:31
 */
public class MyTomcatProtocolHandlerCustomizer implements TomcatProtocolHandlerCustomizer<ProtocolHandler> {

    private TomcatServletWebServerFactory factory;

    public MyTomcatProtocolHandlerCustomizer(TomcatServletWebServerFactory factory) {
        this.factory = factory;
    }

    @Override
    public void customize(ProtocolHandler protocolHandler) {
        if (protocolHandler instanceof AbstractProtocol) {
            AbstractProtocol protocol = (AbstractProtocol) protocolHandler;
            protocol.setMaxThreads(250);
        }
    }
}

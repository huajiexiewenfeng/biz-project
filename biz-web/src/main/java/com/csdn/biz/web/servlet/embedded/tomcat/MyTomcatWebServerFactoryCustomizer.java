package com.csdn.biz.web.servlet.embedded.tomcat;

import org.springframework.boot.web.embedded.tomcat.ConfigurableTomcatWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 自定义 Tomcat {@link WebServerFactoryCustomizer}
 *
 * @Author: xiewenfeng
 * @Date: 2022/12/7 15:03
 */
@Component
public class MyTomcatWebServerFactoryCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>, Ordered {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addProtocolHandlerCustomizers(new MyTomcatProtocolHandlerCustomizer(factory));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

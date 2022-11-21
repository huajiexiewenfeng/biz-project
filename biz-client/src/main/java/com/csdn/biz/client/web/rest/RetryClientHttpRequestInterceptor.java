package com.csdn.biz.client.web.rest;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * 重试
 *
 * @author ：xwf
 * @date ：Created in 2022\11\19 0019 19:17
 */
public class RetryClientHttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = null;
        try {
            response = execution.execute(request, body);
            if (!response.getStatusCode().is2xxSuccessful()) {
                // TODO retry ... feign.SynchronousMethodHandler.invoke
            }
        } catch (Exception e) {
            // TODO retry ...

        }
        return response;
    }

    /**
     * 最低优先级
     *
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

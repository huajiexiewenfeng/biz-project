package com.csdn.biz.api;

import java.util.Collections;
import java.util.Map;
import org.springframework.util.MultiValueMap;

/**
 * @Author: xiewenfeng
 * @Date: 2022/11/14 17:44
 */
public class ApiBase<T> {

  @Deprecated
  private Map<String, String> headers;
  @Deprecated
  private MultiValueMap<String, String> httpHeaders;
  private T body;

  public Map<String, String> getHeaders() {
    if (headers == null) {
      return Collections.emptyMap();
    }
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public T getBody() {
    return body;
  }

  public void setBody(T body) {
    this.body = body;
  }

  public MultiValueMap<String, String> getHttpHeaders() {
    return httpHeaders;
  }

  public void setHttpHeaders(
      MultiValueMap<String, String> httpHeaders) {
    this.httpHeaders = httpHeaders;
  }
}

package com.csdn.biz.api;

import com.csdn.biz.api.enums.StatusCode;
import java.util.Collections;
import java.util.Map;
import org.springframework.util.MultiValueMap;

/**
 * API 请求对象
 *
 * @param <T> T 模型对象类型
 * @Author: xiewenfeng
 * @Date: 2022/11/14 17:03
 */
public class ApiResponse<T> extends ApiBase<T> {

  private int code;

  private String message;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public static <T> ApiResponse<T> ok(T body) {
    return of(body, StatusCode.OK);
  }

  public static <T> ApiResponse<T> failed(T body) {
    return of(body, StatusCode.FAILED);
  }

  public static <T> ApiResponse<T> failed(T body, String errorMessage) {
    ApiResponse<T> response = of(body, StatusCode.FAILED);
    response.setMessage(errorMessage);
    return response;
  }

  public static <T> ApiResponse<T> of(T body, StatusCode statusCode) {
    ApiResponse<T> response = new ApiResponse<T>();
    response.setCode(statusCode.getCode());
    response.setMessage(statusCode.getMessage());
    response.setBody(body);
    return response;
  }

  public static class Builder<T> {

    private int code;

    private String message;

    public Builder<T> code(int code) {
      this.code = code;
      return this;
    }
  }
}

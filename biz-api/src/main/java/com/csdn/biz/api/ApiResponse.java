package com.csdn.biz.api;

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

  private String code;

  private String msg;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}

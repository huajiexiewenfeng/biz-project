package com.csdn.biz.web.mvc.controller;

import com.csdn.biz.api.ApiRequest;
import com.csdn.biz.api.ApiResponse;
import com.csdn.biz.api.interfaces.UserRegistrationRestService;
import com.csdn.biz.api.model.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xiewenfeng
 * @Date: 2022/11/16 15:21
 */
@RestController
public class UserRegistrationRestController implements UserRegistrationRestService {

  @Override
  public ApiResponse<Boolean> register(@RequestBody @Validated User user) {
    return ApiResponse.ok(Boolean.TRUE);
  }

  @Override
  public ApiResponse<Boolean> register(@RequestBody @Validated ApiRequest<User> userRequest) {
    return ApiResponse.ok(Boolean.TRUE);
  }
}

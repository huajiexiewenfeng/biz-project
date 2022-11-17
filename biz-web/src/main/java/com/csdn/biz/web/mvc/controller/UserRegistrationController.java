package com.csdn.biz.web.mvc.controller;

import com.csdn.biz.api.interfaces.UserRegistrationService;
import com.csdn.biz.api.model.User;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xiewenfeng
 * @Date: 2022/11/16 15:21
 */
@RestController
public class UserRegistrationController implements UserRegistrationService {

  @Override
  public Boolean register(User user) {
    return Boolean.TRUE;
  }
}

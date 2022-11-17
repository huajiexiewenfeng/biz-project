package com.csdn.biz.api.interfaces;

import com.csdn.biz.api.model.User;
import java.util.Map;
import javax.validation.Valid;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户登录服务接口
 *
 * @author xiewenfeng
 */
@FeignClient("${user-login.service.name}")
@RequestMapping("/user")
@DubboService
public interface UserRegistrationService {

  @PostMapping(value = "/register/v3", produces = "application/json;v=3.0")
  Boolean register(@RequestBody @Validated @Valid User user);
}

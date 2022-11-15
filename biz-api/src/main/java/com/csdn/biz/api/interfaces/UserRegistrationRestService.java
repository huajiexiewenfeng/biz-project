package com.csdn.biz.api.interfaces;

import com.csdn.biz.api.ApiRequest;
import com.csdn.biz.api.ApiResponse;
import com.csdn.biz.api.model.User;
import java.util.Map;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户登录服务接口
 *
 * @author xiewenfeng
 */
@FeignClient("${user-login.rest-service.name}")
@RequestMapping("/api/user")
@DubboService
public interface UserRegistrationRestService {

  @PostMapping("/login")
  User login(Map<String, Object> context);

  @PostMapping("/login/v1")
  ApiResponse<Boolean> login(User user);

  @PostMapping("/login/v2")
  ApiResponse<Boolean> login(ApiRequest<User> userRequest);
}

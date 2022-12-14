package com.csdn.biz.api.interfaces;

import com.csdn.biz.api.model.User;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户服务接口
 *
 * @author xiewenfeng
 * @see UserLoginService
 * @see UserRegistrationService
 * @deprecated 该接口不推荐使用，请使用 {@link UserLoginService} 或 {@link UserRegistrationService}
 */
@FeignClient("${user-service.name}")
@RequestMapping("/user")
@Deprecated
public interface UserService {

  @PostMapping("/register")
  Boolean register(User user);

  @PostMapping("/login")
  User login(Map<String, Object> context);
}

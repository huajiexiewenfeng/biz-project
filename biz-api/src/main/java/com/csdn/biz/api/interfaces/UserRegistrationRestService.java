package com.csdn.biz.api.interfaces;

import com.csdn.biz.api.ApiRequest;
import com.csdn.biz.api.ApiResponse;
import com.csdn.biz.api.model.User;
import java.util.Map;
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
@FeignClient("${user-login.rest-service.name}")
@DubboService
public interface UserRegistrationRestService {

  @PostMapping(value = "/user/register", produces = "application/json;v=1.0")
  ApiResponse<Boolean> register(@RequestBody @Validated User user);

  @PostMapping(value = "/user/register", produces = "application/json;v=2.0")
  ApiResponse<Boolean> register(@RequestBody @Validated ApiRequest<User> userRequest);
}

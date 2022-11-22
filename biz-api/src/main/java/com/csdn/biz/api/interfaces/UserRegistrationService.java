package com.csdn.biz.api.interfaces;

import com.csdn.biz.api.exeption.UserException;
import com.csdn.biz.api.model.User;

import java.util.Map;
import javax.validation.Valid;

import com.csdn.biz.api.openfeign.UserServiceFeignConfiguration;
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
@FeignClient(value = "${user-registration.service.name:user-service}", path = "/user", configuration = UserServiceFeignConfiguration.class)
@DubboService
public interface UserRegistrationService {

    @PostMapping(value = "/register/v3", produces = "application/json;v=3.0")
    Object register(@RequestBody @Validated @Valid User user) throws UserException;
}

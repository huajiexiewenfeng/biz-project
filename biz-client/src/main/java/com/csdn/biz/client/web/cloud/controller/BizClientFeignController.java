package com.csdn.biz.client.web.cloud.controller;

import com.csdn.biz.api.interfaces.UserRegistrationService;
import com.csdn.biz.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xiewenfeng
 * @Date: 2022/12/26 9:50
 */
@RestController
public class BizClientFeignController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @GetMapping("/user/register")
    public Object registerUser() {
        User user = new User();
        user.setId(1L);
        user.setName("ABC");
        return userRegistrationService.register(user);
    }
}

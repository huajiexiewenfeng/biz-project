package com.csdn.biz.api.interfaces;

import com.csdn.biz.api.model.User;
import org.springframework.web.bind.annotation.PostMapping;

public interface UserService {

    @PostMapping("/save")
    Boolean save(User user);
}

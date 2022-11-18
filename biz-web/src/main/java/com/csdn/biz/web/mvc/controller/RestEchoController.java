package com.csdn.biz.web.mvc.controller;

import com.csdn.biz.api.ApiResponse;
import com.csdn.biz.api.model.User;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: xiewenfeng
 * @Date: 2022/11/18 16:29
 */
@RestController
@RequestMapping("/rest/echo")
public class RestEchoController {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${server.port:8080}")
  private Integer port;

  @PostMapping("/user")
  public ApiResponse<User> echo(@RequestBody User user) {
    return ApiResponse.ok(user);
  }

  @GetMapping("/call/{name}")
  public ApiResponse<User> callEcho(@PathVariable String name) {
    String url = "http://127.0.0.1:{port}/rest/echo/user";
    User user = new User();
    user.setName(name);
    return restTemplate.postForObject(url, user, ApiResponse.class, port);
  }

}

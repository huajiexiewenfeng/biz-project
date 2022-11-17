package com.csdn.biz.api;

import com.csdn.biz.api.model.User;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

/**
 * @Author: xiewenfeng
 * @Date: 2022/11/16 15:00
 */
public class ApiRequestTest extends BaseTest {

  @Test
  public void test() {
    ApiRequest<User> request = new ApiRequest<>();
    request.setBody(new User());
    Set<ConstraintViolation<ApiRequest<User>>> constraintViolations = validate(request);
    constraintViolations.forEach(constraintViolation -> {
      System.out.println(constraintViolation.getMessage());
    });
  }

}

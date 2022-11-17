package com.csdn.biz.api;

import com.csdn.biz.api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.validation.beanvalidation.LocaleContextMessageInterpolator;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.*;
import javax.validation.bootstrap.GenericBootstrap;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ：xwf
 * @date ：Created in 2022\11\15 0015 23:09
 */
public class UserTest extends BaseTest {

  @Test
  public void test() {
    User user = new User();
    user.setName("456");
//    user.setId(123L);
    Set<ConstraintViolation<User>> constraintViolations = validate(user);
    constraintViolations.forEach(constraintViolation -> {
      System.out.println(constraintViolation.getMessage());
    });
  }
}

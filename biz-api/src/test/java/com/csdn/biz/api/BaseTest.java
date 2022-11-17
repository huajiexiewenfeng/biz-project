package com.csdn.biz.api;

import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.validation.beanvalidation.LocaleContextMessageInterpolator;

/**
 * @Author: xiewenfeng
 * @Date: 2022/11/16 15:03
 */
public class BaseTest {

  private Validator validator;

  @BeforeEach
  public void init() {
    GenericBootstrap bootstrap = Validation.byDefaultProvider();
    Configuration<?> configuration = bootstrap.configure();

    MessageInterpolator targetInterpolator = configuration.getDefaultMessageInterpolator();
    configuration.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));

    ValidatorFactory validatorFactory = configuration.buildValidatorFactory();
    this.validator = validatorFactory.getValidator();
  }

  protected <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
    return validator.validate(object, groups);
  }
}

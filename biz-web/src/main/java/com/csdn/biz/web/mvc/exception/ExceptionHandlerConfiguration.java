package com.csdn.biz.web.mvc.exception;

import com.csdn.biz.api.ApiResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;

/**
 * @author ：xwf
 * @date ：Created in 2022\11\15 0015 23:36
 */
@ControllerAdvice
public class ExceptionHandlerConfiguration {

    @ExceptionHandler(ValidationException.class)
    public ApiResponse<Void> onValidationException(ValidationException exception) {
        return ApiResponse.failed(null, exception.getMessage());
    }
}

package com.csdn.biz.web.mvc.exception;

import com.csdn.biz.api.ApiResponse;
import javax.validation.ValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ：xwf
 * @date ：Created in 2022\11\15 0015 23:36
 */
@RestControllerAdvice
public class ExceptionHandlerConfiguration {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> onMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ApiResponse.failed(null, exception.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ApiResponse<Void> onValidationException(ValidationException exception) {
        return ApiResponse.failed(null, exception.getMessage());
    }
}

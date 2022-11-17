package com.csdn.biz.web.mvc.method.annotation;

import com.csdn.biz.api.ApiResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * 自定义的 MethodReturnValueHandler，用于将 POJO 对象封装成 ApiResponse
 *
 * @Author: xiewenfeng
 * @Date: 2022/11/16 17:53
 * @see RequestResponseBodyMethodProcessor
 */
public class ApiResponseHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

  private MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

  @Override
  public boolean supportsReturnType(MethodParameter returnType) {
    return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class)
        ||
        returnType.hasMethodAnnotation(ResponseBody.class))
        && !ApiResponse.class.equals(returnType.getParameterType());
  }

  @Override
  public void handleReturnValue(Object returnValue, MethodParameter returnType,
      ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
    mavContainer.setRequestHandled(true);
    ApiResponse apiResponse = ApiResponse.ok(returnValue);
    ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
    converter.write(apiResponse, MediaType.APPLICATION_JSON, outputMessage);
  }

  /**
   * Creates a new {@link HttpOutputMessage} from the given {@link NativeWebRequest}.
   *
   * @param webRequest the web request to create an output message from
   * @return the output message
   */
  protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
    HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
    Assert.state(response != null, "No HttpServletResponse");
    return new ServletServerHttpResponse(response);
  }

}

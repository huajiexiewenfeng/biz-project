package com.csdn.biz.web.servlet.idempotent;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @Author: xiewenfeng
 * @Date: 2022/11/17 17:03
 */
public class IdempotentFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // 使用 HttpSession Id -> cookie 来自于 headers
    String token = request.getParameter("token");

    // HttpSession 与 redis 打通，利用 Spring Session
    HttpSession session = request.getSession(false);

    Object value = session.getAttribute(token);

    if (value != null) {
      // 抛出异常
      throw new ServletException("幂等性校验错误");
    } else {
      // 设置状态
      session.setAttribute(token, token);
      try {
        // 处理
        filterChain.doFilter(request, response);
      } finally {
        // 移除状态
        session.removeAttribute(token);
      }
    }
  }
}

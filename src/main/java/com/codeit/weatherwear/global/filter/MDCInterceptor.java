package com.codeit.weatherwear.global.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Order: 빈의 우선순위 부여 (가장 높은 우선순위 부여한 상황)
 */

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCInterceptor implements HandlerInterceptor {

  /**
   * Populates the SLF4J Mapped Diagnostic Context with request-scoped values before controller handling.
   *
   * Adds a unique `requestId`, the HTTP method, and the request URI to the MDC so subsequent logging can include them.
   *
   * @param request the incoming HTTP request
   * @param response the HTTP response
   * @param handler the chosen handler to execute, for type inspection
   * @return `true` to continue processing the request
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // 요청 ID, 요청 메서드, 요청 uri 로깅
    MDC.put("requestId", UUID.randomUUID().toString());
    MDC.put("method", request.getMethod());
    MDC.put("uri", request.getRequestURI());
    return true;
  }

  /**
   * Clears the SLF4J Mapped Diagnostic Context (MDC) after the request has completed.
   *
   * @param ex the exception thrown during handler execution, if any
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    MDC.clear();
  }
}
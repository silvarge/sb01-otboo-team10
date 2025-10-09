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
   * Populates the MDC with request-scoped identifiers for the current HTTP request.
   *
   * Sets the following MDC keys: "requestId" (a new UUID string), "method" (HTTP method),
   * and "uri" (request URI).
   *
   * @return true to continue processing the request
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
   * Clears the SLF4J Mapped Diagnostic Context (MDC) after a request has completed.
   *
   * @param ex the exception thrown during request processing, or null if none
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    MDC.clear();
  }
}
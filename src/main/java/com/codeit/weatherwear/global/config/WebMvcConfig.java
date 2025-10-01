package com.codeit.weatherwear.global.config;

import com.codeit.weatherwear.global.filter.MDCInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final MDCInterceptor mdcInterceptor;

  private static final String[] EXCLUDE_PATTERNS = {
      "/actuator/**", "/favicon.ico", "/error", "/css/**", "/js/**", "/images/**", "/webjars/**"
  };

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(mdcInterceptor)
        .order(Ordered.HIGHEST_PRECEDENCE)
        .addPathPatterns("/**")
        .excludePathPatterns(EXCLUDE_PATTERNS);
  }
}

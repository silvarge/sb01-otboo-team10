package com.codeit.weatherwear.domain.weather.api.strategy.impl;

import com.codeit.weatherwear.domain.weather.api.strategy.KoreanWeatherApiStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 공공 데이터 포털의 날씨 예보 API 전략
 */
@Slf4j
@Component
public class OpenWeatherApiStrategy extends KoreanWeatherApiStrategy {

  @Override
  protected String getServiceKeyParamName() {
    return "serviceKey";
  }

  @Override
  public boolean supports(String apiName) {
    return apiName.startsWith("open-");
  }
}

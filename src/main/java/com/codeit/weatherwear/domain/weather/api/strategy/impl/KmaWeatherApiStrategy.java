package com.codeit.weatherwear.domain.weather.api.strategy.impl;

import com.codeit.weatherwear.domain.weather.api.strategy.KoreanWeatherApiStrategy;
import com.codeit.weatherwear.domain.weather.api.strategy.WeatherApiStrategy;
import com.codeit.weatherwear.domain.weather.config.WeatherApiProperties.ApiEndpoint;
import com.codeit.weatherwear.domain.weather.exception.WeatherApiRequestException;
import com.codeit.weatherwear.domain.weather.exception.WeatherApiResponseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 기상자료개방포털(기상청)의 날씨 예보 API 전략
 */
@Slf4j
@Component
public class KmaWeatherApiStrategy extends KoreanWeatherApiStrategy {

  @Override
  protected String getServiceKeyParamName() {
    return "authKey";
  }

  @Override
  public boolean supports(String apiName) {
    return apiName.startsWith("kma-");
  }
}

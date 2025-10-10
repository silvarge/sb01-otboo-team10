package com.codeit.weatherwear.domain.weather.api.strategy;

import com.codeit.weatherwear.domain.weather.config.WeatherApiProperties.ApiEndpoint;
import com.codeit.weatherwear.domain.weather.exception.WeatherApiRequestException;
import com.codeit.weatherwear.domain.weather.exception.WeatherApiResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;

public interface WeatherApiStrategy {

  boolean supports(String apiName);

  String fetchData(
      HttpClient httpClient,
      ObjectMapper mapper,
      ApiEndpoint endpoint,
      String baseDate,
      String baseTime,
      int nx,
      int ny
  ) throws WeatherApiRequestException, WeatherApiResponseException;
}

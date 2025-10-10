package com.codeit.weatherwear.domain.weather.exception;

import com.codeit.weatherwear.global.exception.CustomException;
import com.codeit.weatherwear.global.exception.ErrorCode;
import java.util.Map;

public class WeatherApiRequestException extends CustomException {

  public WeatherApiRequestException() {
    super(ErrorCode.WEATHER_API_REQUEST_ERROR);
  }

  public WeatherApiRequestException(String msg, Exception lastException) {
    super(ErrorCode.WEATHER_API_REQUEST_ERROR, Map.of("message", msg, "lastException", lastException));
  }
}

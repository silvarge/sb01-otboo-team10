package com.codeit.weatherwear.domain.weather.exception;

import com.codeit.weatherwear.global.exception.CustomException;
import com.codeit.weatherwear.global.exception.ErrorCode;
import java.util.Map;

public class UseStrategyNotFoundException extends CustomException {

  public UseStrategyNotFoundException() {
    super(ErrorCode.WEATHER_REQUEST_API_NOT_FOUND);
  }

  public UseStrategyNotFoundException(String apiName) {
    super(ErrorCode.WEATHER_REQUEST_API_NOT_FOUND, Map.of("apiName", apiName));
  }
}

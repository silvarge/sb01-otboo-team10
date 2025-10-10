package com.codeit.weatherwear.domain.weather.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weather")
public record WeatherApiProperties(List<ApiEndpoint> endPoints) {

  public record ApiEndpoint(String name, String apiUrl, String apiServiceKey, int priority,
                            boolean enabled) {

  }
}

package com.codeit.weatherwear.domain.weather.api;

import com.codeit.weatherwear.domain.weather.api.strategy.WeatherApiStrategy;
import com.codeit.weatherwear.domain.weather.config.WeatherApiProperties;
import com.codeit.weatherwear.domain.weather.config.WeatherApiProperties.ApiEndpoint;
import com.codeit.weatherwear.domain.weather.exception.WeatherApiRequestException;
import com.codeit.weatherwear.domain.weather.exception.WeatherApiResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 기상청 단기 예보 API와 통신하여 데이터를 요청하는 역할
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherApiClient {

  private final WeatherApiProperties apiProperties;
  private final HttpClient httpClient;
  private final ObjectMapper mapper;
  private final List<WeatherApiStrategy> strategies;  // API 별 전략

  /**
   * 단기 예보 API에 데이터를 요청하여 응답을 받아와 리턴한다.
   *
   * @param mapper   JSON 변환에 사용할 ObjectMapper
   * @param baseDate 예보 기준 날짜 (yyyyMMdd)
   * @param baseTime 예보 기준 시간 (HHmm)
   * @param nx       예보 지점 X좌표
   * @param ny       예보 지점 Y좌표
   * @return API 응답 데이터(ResponseBody)
   */
  public String fetchWeatherData(String baseDate, String baseTime, int nx, int ny) {
    // 우선 순위 순으로 엔드포인트 정렬
    List<ApiEndpoint> activeEndpoints = apiProperties.endPoints().stream()
        .filter(WeatherApiProperties.ApiEndpoint::enabled)
        .sorted(Comparator.comparingInt(WeatherApiProperties.ApiEndpoint::priority))
        .toList();

    if (activeEndpoints.isEmpty()) {
      // todo: 요청 할 수 있는 엔드포인트가 없다는 예외 추가
      throw new WeatherApiRequestException();
    }

    // 마지막에 발생한 예외 기록용 - 어디까지 잘못됐나 추적
    Exception lastException = null;

    for (ApiEndpoint endpoint : activeEndpoints) {
      try {
        log.info("Attempting Weather API: {} (priority: {})", endpoint.name(), endpoint.priority());

        WeatherApiStrategy strategy = findStrategy(endpoint.name());
        String responseBody = strategy.fetchData(httpClient, mapper, endpoint, baseDate, baseTime,
            nx, ny);

        log.info("Successfully fetched weather data from: {}", endpoint.name());
        return responseBody;

      } catch (WeatherApiResponseException | WeatherApiRequestException we) {
        log.warn("Failed to fetch from {}: {}", endpoint.name(), we.getMessage());
        lastException = we;
      }
    }

    log.error("All weather forecast API endpoints failed");
    // todo: 예외 처리 추후 조심해야 함
    throw new WeatherApiRequestException("All Configured weather APIs failed", lastException);
  }

  private WeatherApiStrategy findStrategy(String apiName) {
    return strategies.stream()
        .filter(s -> s.supports(apiName))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            "No Strategy found for Weather Forecast API: " + apiName));
  }

}

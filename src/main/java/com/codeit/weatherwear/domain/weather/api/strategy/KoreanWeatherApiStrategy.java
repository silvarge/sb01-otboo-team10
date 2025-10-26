package com.codeit.weatherwear.domain.weather.api.strategy;

import com.codeit.weatherwear.domain.weather.config.WeatherApiProperties.ApiEndpoint;
import com.codeit.weatherwear.domain.weather.exception.WeatherApiRequestException;
import com.codeit.weatherwear.domain.weather.exception.WeatherApiResponseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * 한국 API (공공 데이터 포탈/기상청 Open API 로직)
 * <p>
 * API Key 파라미터명 제외하고 모두 동일하기에 추상 클래스를 통해 추상화하고 <br> 각 API별 구현체에서는 둘이 다른 부분만 조정할 수 있도록 함
 * <p>
 * 중복 로직 제거 목적
 */
@Slf4j
public abstract class KoreanWeatherApiStrategy implements WeatherApiStrategy {

  private static final String DATA_TYPE = "JSON";
  private static final int NUM_OF_ROWS = 1500;
  private static final String SUCCESS_RESULT_CODE = "00";

  protected abstract String getServiceKeyParamName();

  @Override
  public String fetchData(HttpClient httpClient, ObjectMapper mapper, ApiEndpoint endpoint,
      String baseDate, String baseTime, int nx, int ny)
      throws WeatherApiRequestException, WeatherApiResponseException {
    // 요청 URL 세팅
    String requestUrl = buildRequestUrl(endpoint, baseDate, baseTime, nx, ny);

    // HttpClient 세팅 및 요청 세팅
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(requestUrl))
        .GET()
        .build();

    try {
      HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
      String resultCode = extractResultCode(mapper, response.body());
      // 응답 코드가 00이 아니면 비정상적인 응답 (오류)
      if (!SUCCESS_RESULT_CODE.equals(resultCode)) {
        log.warn("Weather Api Response Invalid");
        throw new WeatherApiResponseException(resultCode);
      }
      // 응답 Body 전달
      return response.body();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.info("url: {}", requestUrl);
      log.error("Weather Api Request Invalid - cause: {}\nmessage: {}", e.getCause(),
          e.getMessage());
      throw new WeatherApiRequestException("Weather API request interrupted", e);
    } catch (IOException e) {
      log.info("url: {}", requestUrl);
      log.error("Weather Api Request Invalid - cause: {}\nmessage: {}", e.getCause(),
          e.getMessage());
      throw new WeatherApiRequestException();
      throw new WeatherApiRequestException("Weather API request failed", e);
    }
  }

  private String buildRequestUrl(ApiEndpoint endpoint, String baseDate, String baseTime, int nx,
      int ny) {
    return String.format(
        "%s?%s=%s&numOfRows=%d&dataType=%s&base_date=%s&base_time=%s&nx=%d&ny=%d",
        endpoint.apiUrl(), getServiceKeyParamName(),
        URLEncoder.encode(endpoint.apiServiceKey(), StandardCharsets.UTF_8),
        NUM_OF_ROWS, DATA_TYPE, baseDate, baseTime, nx, ny
    );
  }

  private String extractResultCode(ObjectMapper mapper, String responseBody)
      throws IOException {
    // Header의 resultCode 필드 속 값 String 형태로 추출
    JsonNode root = mapper.readTree(responseBody);
    return root.path("response").path("header").path("resultCode").asText();
  }
}

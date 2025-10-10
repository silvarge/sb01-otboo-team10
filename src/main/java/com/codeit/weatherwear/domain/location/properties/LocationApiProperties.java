package com.codeit.weatherwear.domain.location.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "location")
public record LocationApiProperties(String apiUrl, String apiKey) {

}

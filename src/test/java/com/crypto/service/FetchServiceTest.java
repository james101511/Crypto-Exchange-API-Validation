package com.crypto.service;

import static com.crypto.enums.TimeFrame.ONE_MINUTE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.crypto.config.CryptoProperties;
import com.crypto.dto.CandleStickResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
public class FetchServiceTest {

  @InjectMocks
  private FetchService fetchService;
  @Mock
  private RestTemplate restTemplate;
  @Mock
  private CryptoProperties properties;

  @Test
  @DisplayName("404 exception")
  void notFoundException_shouldThrow() {
    given(properties.getCandleStickUrl("BTC_USDT", ONE_MINUTE.getCode())).willReturn("wrong url");
    given(restTemplate.getForEntity("wrong url", CandleStickResponse.class))
        .willThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    assertThatThrownBy(
        () -> fetchService.getCandleSticks("BTC_USDT", ONE_MINUTE)).isInstanceOf(
        HttpClientErrorException.class);
  }

  @Test
  @DisplayName("500 exception")
  void serverError_shouldThrow() {
    given(properties.getCandleStickUrl("BTC_USDT", ONE_MINUTE.getCode())).willReturn("wrong url");
    given(restTemplate.getForEntity("wrong url", CandleStickResponse.class))
        .willThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    assertThatThrownBy(
        () -> fetchService.getCandleSticks("BTC_USDT", ONE_MINUTE)).isInstanceOf(
        HttpServerErrorException.class);
  }
}

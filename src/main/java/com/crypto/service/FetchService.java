package com.crypto.service;

import com.crypto.config.CryptoProperties;
import com.crypto.dto.CandleStickResponse;
import com.crypto.dto.TradeResponse;
import com.crypto.enums.TimeFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class FetchService {

  private final RestTemplate restTemplate;

  private final CryptoProperties properties;

  public CandleStickResponse getCandleSticks(String instrumentName, TimeFrame timeframe)
      throws HttpClientErrorException, HttpServerErrorException {
    return restTemplate.getForEntity(
        properties.getCandleStickUrl(instrumentName, timeframe.getCode()),
        CandleStickResponse.class).getBody();
  }

  public TradeResponse getTrades(String instrumentName)
      throws HttpClientErrorException, HttpServerErrorException {
    return restTemplate.getForEntity(
        properties.getTradeUrl(instrumentName),
        TradeResponse.class).getBody();
  }
}

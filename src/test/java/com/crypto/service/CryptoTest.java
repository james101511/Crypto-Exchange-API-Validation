package com.crypto.service;

import static com.crypto.enums.TimeFrame.ONE_MINUTE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.crypto.dto.CandleStickResponse;
import com.crypto.dto.CandleStickResult;
import com.crypto.dto.TradeResponse;
import com.crypto.dto.TradeResult;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@SpringBootTest
class CryptoTest {

  @InjectMocks
  private CryptoService cryptoService;

  @Mock
  private FetchService fetchService;

  @Spy
  private CandleService candleService;

  @Test
  @DisplayName("if one of the api return client error, checking process will not be invoked")
  void clientError_shouldNotInvokeProcess() {
    given(fetchService.getCandleSticks("BTC_USDT", ONE_MINUTE)).willThrow(
        new HttpClientErrorException(HttpStatus.NOT_FOUND));
    cryptoService.startVerify("BTC_USDT", ONE_MINUTE);
    verify(candleService, never()).checkCandleStickConsistOfTrades(any(), any());
    verify(candleService, never()).separateTradesByCandleTime(any(), any());
  }

  @Test
  @DisplayName("if one of the api return server error, checking process will not be invoked")
  void serverError_shouldNotInvokeProcess() {
    given(fetchService.getCandleSticks("BTC_USDT", ONE_MINUTE)).willThrow(
        new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
    cryptoService.startVerify("BTC_USDT", ONE_MINUTE);
    verify(candleService, never()).checkCandleStickConsistOfTrades(any(), any());
    verify(candleService, never()).separateTradesByCandleTime(any(), any());
  }

  @Test
  @DisplayName("if one of the result from api is empty, the checking process will not be invoked")
  void responseEmpty_shouldNotInvokeProcess() {
    given(fetchService.getTrades("BTC_USDT")).willReturn(dummyTradeResponse());
    given(fetchService.getCandleSticks("BTC_USDT", ONE_MINUTE)).willReturn(dummyCandleResponse());
    cryptoService.startVerify("BTC_USDT", ONE_MINUTE);
    verify(candleService, never()).checkCandleStickConsistOfTrades(any(), any());
    verify(candleService, never()).separateTradesByCandleTime(any(), any());
  }


  public CandleStickResponse dummyCandleResponse() {
    CandleStickResult result = CandleStickResult.builder()
        .data(Lists.newArrayList())
        .build();
    CandleStickResponse response = new CandleStickResponse();
    response.setResult(result);
    return response;
  }

  public TradeResponse dummyTradeResponse() {
    TradeResult result = TradeResult.builder()
        .data(Lists.newArrayList())
        .build();
    TradeResponse response = new TradeResponse();
    response.setResult(result);
    return response;
  }
}

package com.crypto.service;

import com.crypto.entity.CandleStick;
import com.crypto.entity.Trade;
import com.crypto.enums.TimeFrame;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CryptoService {

  private final CandleService candleService;

  private final FetchService fetchService;

  public void startVerify(String instrumentName, TimeFrame timeframe) {
    try {
      val candleStick = fetchService.getCandleSticks(instrumentName, timeframe);
      val trades = fetchService.getTrades(instrumentName);
      val candleData = candleStick.getResult().getSortedData();
      val tradeData = trades.getResult().getSortedData();
      if (CollectionUtils.isEmpty(candleData) || CollectionUtils.isEmpty(tradeData)) {
        log.info("no data included");
        return;
      }
      Map<CandleStick, List<Trade>> tradesByCandles = candleService.separateTradesByCandleTime(
          candleData, tradeData);
      tradesByCandles.forEach(candleService::checkCandleStickConsistOfTrades);
    } catch (HttpClientErrorException e) {
      log.info("http client error {}", e.getMessage());
    } catch (HttpServerErrorException e) {
      log.info("http server error {}", e.getMessage());
    } catch (RestClientException e) {
      log.info("http connect error {}", e.getMessage());
    }
  }
}

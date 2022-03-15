package com.crypto.service;

import com.crypto.entity.CandleStick;
import com.crypto.entity.Trade;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandleService {

  public Map<CandleStick, List<Trade>> separateTradesByCandleTime(
      List<CandleStick> candleSticks,
      List<Trade> trades) {
    Map<CandleStick, List<Trade>> result = new TreeMap<>();
    int lastIndex = -1;
    for (CandleStick candleStick : candleSticks) {
      List<Trade> intervalTrades = Lists.newArrayList();
      val start = candleStick.getStartTime();
      val end = candleStick.getEndTime();
      int startIndex = lastIndex + 1;
      for (int i = startIndex; i < trades.size(); i++) {
        val tradeTime = trades.get(i).getTradeTime();
        if (tradeTime.isBefore(start)) {
          continue;
        }
        //sharp time will be counted into the previous one
        if (tradeTime.isAfter(end)) {
          break;
        }
        intervalTrades.add(trades.get(i));
        lastIndex = i;
      }
      result.put(candleStick, intervalTrades);
    }

    return result;
  }

  public boolean checkCandleStickConsistOfTrades(CandleStick candleStick, List<Trade> trades) {
    val startTime = candleStick.getStartTime();
    val endTime = candleStick.getEndTime();
    if (CollectionUtils.isEmpty(trades)) {
      log.info("candlestick from {} to {} has no trades", startTime, endTime);
      return false;
    }
    CandleStick candleFromTrades =
        new CandleStick(trades, candleStick.getTimeFrame(), startTime, endTime);

    boolean isValid = candleStick.checkDataConsist(candleFromTrades);

    log.info("candlestick from {} to {} is {} with {} trades",
        startTime,
        endTime,
        isValid ? "valid" : "invalid",
        trades.size()
    );

    if (!isValid) {
      log.info("candleStick has open: {}, close: {}, high: {}, low: {}",
          candleStick.getOpen(),
          candleStick.getClose(),
          candleStick.getHigh(),
          candleStick.getLow()
      );
      log.info("realCandleStick has open: {}, close: {}, high: {}, low: {}",
          candleFromTrades.getOpen(),
          candleFromTrades.getClose(),
          candleFromTrades.getHigh(),
          candleFromTrades.getLow()
      );
    }
    return isValid;

  }

}

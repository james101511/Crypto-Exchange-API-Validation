package com.crypto.entity;

import com.crypto.enums.TimeFrame;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CandleStick implements Comparable<CandleStick> {

  private Instant startTime;
  private Instant endTime;
  private BigDecimal open;
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private TimeFrame timeFrame;

  public CandleStick(List<Trade> sortedTrades, TimeFrame timeFrame, Instant startTime,
      Instant endTime) {
    this.timeFrame = timeFrame;
    this.startTime = startTime;
    this.endTime = endTime;
    this.open = sortedTrades.get(0).getPrice();
    this.close = sortedTrades.get(sortedTrades.size() - 1).getPrice();
    this.high = open;
    this.low = open;
    for (Trade trade : sortedTrades) {
      val price = trade.getPrice();
      high = high.compareTo(price) > 0 ? high : price;
      low = low.compareTo(price) < 0 ? low : price;
    }
  }

  public boolean isOpenPriceEqual(BigDecimal price) {
    return open.compareTo(price) == 0;
  }

  public boolean isClosePriceEqual(BigDecimal price) {
    return close.compareTo(price) == 0;
  }

  public boolean isHighestPriceEqual(BigDecimal price) {
    return high.compareTo(price) == 0;
  }

  public boolean isLowestPriceEqual(BigDecimal price) {
    return low.compareTo(price) == 0;
  }

  @Override
  public int compareTo(CandleStick candle) {
    return candle.getEndTime().compareTo(endTime);
  }

  public boolean checkDataConsist(CandleStick candle) {
    return isOpenPriceEqual(candle.getOpen()) &&
        isClosePriceEqual(candle.getClose()) &&
        isHighestPriceEqual(candle.getHigh()) &&
        isLowestPriceEqual(candle.getLow());
  }
}

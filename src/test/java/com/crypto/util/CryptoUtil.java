package com.crypto.util;

import com.crypto.entity.CandleStick;
import com.crypto.entity.Trade;
import com.crypto.enums.TimeFrame;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CryptoUtil {

  public static Instant NOW = Instant.parse("2022-03-12T16:00:00Z");
  public static Instant NEXT_ONE_SECOND = NOW.plus(1, ChronoUnit.SECONDS);
  public static Instant NEXT_FIVE_SECONDS = NOW.plus(5, ChronoUnit.SECONDS);
  public static Instant NEXT_ONE_MINUTE = NOW.plus(1, ChronoUnit.MINUTES);
  public static Instant NEXT_NINETY_SECONDS = NOW.plus(90, ChronoUnit.SECONDS);
  public static Instant NEXT_FOUR_HOURS = NOW.plus(4, ChronoUnit.HOURS);
  public static Instant NEXT_FIVE_HOURS = NOW.plus(5, ChronoUnit.HOURS);
  public static Instant NEXT_TEN_HOURS = NOW.plus(10, ChronoUnit.HOURS);

  public static Trade dummyTrade(BigDecimal price, Instant dateTime, String pairs) {
    return Trade.builder()
        .instrument(pairs)
        .price(price)
        .tradeTime(dateTime)
        .build();
  }

  public static Trade dummyTradeWithPriceTen(Instant dateTime, String pairs) {
    return Trade.builder()
        .instrument(pairs)
        .price(BigDecimal.TEN)
        .tradeTime(dateTime)
        .build();
  }


  public static CandleStick dummyCandle(
      TimeFrame timeFrame,
      BigDecimal open,
      BigDecimal close,
      BigDecimal high,
      BigDecimal low,
      Instant startTime
  ) {
    return CandleStick.builder()
        .timeFrame(timeFrame)
        .open(open)
        .close(close)
        .high(high)
        .low(low)
        .startTime(startTime)
        .endTime(startTime.plus(timeFrame.getMin(), ChronoUnit.MINUTES))
        .build();
  }

  public static CandleStick dummyCandleWithFixedPrice(
      TimeFrame timeFrame,
      Instant startTime
  ) {
    return CandleStick.builder()
        .timeFrame(timeFrame)
        .open(BigDecimal.TEN)
        .close(BigDecimal.TEN)
        .high(BigDecimal.TEN)
        .low(BigDecimal.TEN)
        .startTime(startTime)
        .endTime(startTime.plus(timeFrame.getMin(), ChronoUnit.MINUTES))
        .build();
  }

}

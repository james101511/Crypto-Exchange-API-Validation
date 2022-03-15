package com.crypto.service;

import static com.crypto.util.CryptoUtil.NEXT_FIVE_HOURS;
import static com.crypto.util.CryptoUtil.NEXT_FIVE_SECONDS;
import static com.crypto.util.CryptoUtil.NEXT_FOUR_HOURS;
import static com.crypto.util.CryptoUtil.NEXT_NINETY_SECONDS;
import static com.crypto.util.CryptoUtil.NEXT_ONE_MINUTE;
import static com.crypto.util.CryptoUtil.NEXT_ONE_SECOND;
import static com.crypto.util.CryptoUtil.NEXT_TEN_HOURS;
import static com.crypto.util.CryptoUtil.NOW;
import static com.crypto.util.CryptoUtil.dummyCandle;
import static com.crypto.util.CryptoUtil.dummyCandleWithFixedPrice;
import static com.crypto.util.CryptoUtil.dummyTrade;
import static com.crypto.util.CryptoUtil.dummyTradeWithPriceTen;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

import com.crypto.enums.TimeFrame;
import java.math.BigDecimal;
import lombok.val;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CandleServiceTest {

  CandleService service = new CandleService();


  @Test
  @DisplayName("given 4 trades and two candles, two of trades are in the first candle, one is in the other candle, last one is out of range")
  void filterTradesByCandle_shouldReturnMapWithTwoTradesAndOne() {
    val firstTrade = dummyTradeWithPriceTen(NEXT_ONE_SECOND, "BTC/USDT");
    val secondTrade = dummyTradeWithPriceTen(NEXT_FIVE_SECONDS, "BTC/USDT");
    val thirdTrade = dummyTradeWithPriceTen(NEXT_NINETY_SECONDS, "BTC/USDT");
    val tradeNotInRange = dummyTradeWithPriceTen(NEXT_TEN_HOURS, "BTC/USDT");
    val trades = Lists.newArrayList(firstTrade, secondTrade, thirdTrade, tradeNotInRange);

    val firstCandle = dummyCandleWithFixedPrice(
        TimeFrame.ONE_MINUTE,
        NOW
    );
    val secondCandle = dummyCandleWithFixedPrice(
        TimeFrame.ONE_MINUTE,
        NEXT_ONE_MINUTE
    );
    val candles = Lists.newArrayList(firstCandle, secondCandle);
    val tradesByCandleTime = service.separateTradesByCandleTime(candles, trades);
    assertThat(tradesByCandleTime.get(firstCandle), contains(firstTrade, secondTrade));
    assertThat(tradesByCandleTime.get(firstCandle), hasSize(2));
    assertThat(tradesByCandleTime.get(secondCandle), contains(thirdTrade));
    assertThat(tradesByCandleTime.get(secondCandle), hasSize(1));
  }

  @Test
  @DisplayName("given 4 trades and two candles with 4 hours time frame")
  void filterTradesInFourHoursByCandle_shouldReturnMapWithTwoTradesAndOne() {
    val firstTrade = dummyTradeWithPriceTen(NEXT_ONE_SECOND, "BTC/USDT");
    val secondTrade = dummyTradeWithPriceTen(NEXT_FIVE_SECONDS, "BTC/USDT");
    val thirdTrade = dummyTradeWithPriceTen(NEXT_FIVE_HOURS, "BTC/USDT");
    val tradeNotInRange = dummyTradeWithPriceTen(NEXT_TEN_HOURS, "BTC/USDT");
    val trades = Lists.newArrayList(firstTrade, secondTrade, thirdTrade, tradeNotInRange);

    val firstCandle = dummyCandleWithFixedPrice(
        TimeFrame.FOUR_HOURS,
        NOW
    );
    val secondCandle = dummyCandleWithFixedPrice(
        TimeFrame.FOUR_HOURS,
        NEXT_FOUR_HOURS
    );
    val candles = Lists.newArrayList(firstCandle, secondCandle);
    val tradesByCandleTime = service.separateTradesByCandleTime(candles, trades);
    assertThat(tradesByCandleTime.get(firstCandle), contains(firstTrade, secondTrade));
    assertThat(tradesByCandleTime.get(firstCandle), hasSize(2));
    assertThat(tradesByCandleTime.get(secondCandle), contains(thirdTrade));
    assertThat(tradesByCandleTime.get(secondCandle), hasSize(1));
  }

  @Test
  @DisplayName("given one trade and one candle with correct price")
  void isCandleStickValid_singleTrade_shouldReturnTrue() {
    val trade = dummyTrade(TEN, NOW, "BTC/USDT");
    val candle = dummyCandle(
        TimeFrame.ONE_MINUTE,
        TEN,
        TEN,
        TEN,
        TEN,
        NOW
    );
    assertThat(service.checkCandleStickConsistOfTrades(candle, Lists.newArrayList(trade))).isTrue();
  }


  @Test
  @DisplayName("given no trade and one candle")
  void isCandleStickValid_noTrade_shouldReturnFalse() {
    val candle = dummyCandle(
        TimeFrame.ONE_MINUTE,
        TEN,
        TEN,
        TEN,
        TEN,
        NOW
    );
    assertThat(service.checkCandleStickConsistOfTrades(candle, Lists.newArrayList())).isFalse();
  }

  @Test
  @DisplayName("given trades which has the wrong open price")
  void isCandleStickValid_tradesWithWrongOpen_shouldReturnFalse() {
    val firstTrade = dummyTrade(new BigDecimal(1000), NEXT_ONE_MINUTE, "BTC/USDT");
    val trade = dummyTrade(TEN, NEXT_NINETY_SECONDS, "BTC/USDT");

    val candle = dummyCandle(
        TimeFrame.FIVE_MINUTES,
        TEN,
        TEN,
        new BigDecimal(1000),
        TEN,
        NOW
    );
    assertThat(
        service.checkCandleStickConsistOfTrades(candle, Lists.newArrayList(firstTrade, trade)))
        .isFalse();
  }

  @Test
  @DisplayName("given trades which has the wrong close price")
  void isCandleStickValid_tradesWithWrongClose_shouldReturnFalse() {
    val trade = dummyTrade(new BigDecimal(1000), NEXT_ONE_MINUTE, "BTC/USDT");
    val lastTrade = dummyTrade(TEN, NEXT_NINETY_SECONDS, "BTC/USDT");

    val candle = dummyCandle(
        TimeFrame.FIVE_MINUTES,
        TEN,
        new BigDecimal(1000),
        new BigDecimal(1000),
        TEN,
        NOW
    );
    assertThat(
        service.checkCandleStickConsistOfTrades(candle, Lists.newArrayList(trade, lastTrade)))
        .isFalse();
  }

  @Test
  @DisplayName("given trades which has the wrong highest price")
  void isCandleStickValid_tradesWithWrongHighest_shouldReturnFalse() {
    val highest = dummyTrade(new BigDecimal(1000), NEXT_ONE_MINUTE, "BTC/USDT");
    val trade = dummyTrade(TEN, NEXT_NINETY_SECONDS, "BTC/USDT");

    val candle = dummyCandle(
        TimeFrame.FIVE_MINUTES,
        new BigDecimal(1000),
        TEN,
        new BigDecimal(500),
        TEN,
        NOW
    );
    assertThat(service.checkCandleStickConsistOfTrades(candle, Lists.newArrayList(highest, trade)))
        .isFalse();
  }

  @Test
  @DisplayName("given trades which has the wrong lowest price")
  void isCandleStickValid_tradesWithWrongLowest_shouldReturnFalse() {
    val trade = dummyTrade(new BigDecimal(1), NEXT_ONE_MINUTE, "BTC/USDT");
    val lastTrade = dummyTrade(TEN, NEXT_NINETY_SECONDS, "BTC/USDT");

    val candle = dummyCandle(
        TimeFrame.FIVE_MINUTES,
        new BigDecimal(1),
        TEN,
        new BigDecimal(2),
        TEN,
        NOW
    );
    assertThat(
        service.checkCandleStickConsistOfTrades(candle, Lists.newArrayList(trade, lastTrade)))
        .isFalse();
  }
}

package com.crypto.dto;

import com.crypto.config.LongToInstantConverter;
import com.crypto.entity.CandleStick;
import com.crypto.enums.TimeFrame;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandleStickData {

  @JsonProperty("t")
  @JsonDeserialize(converter = LongToInstantConverter.class)
  //it is endTime on doc, however, the data looks more like startTime.
  private Instant startTime;
  @JsonProperty("o")
  private BigDecimal open;
  @JsonProperty("h")
  private BigDecimal high;
  @JsonProperty("l")
  private BigDecimal low;
  @JsonProperty("c")
  private BigDecimal close;
  @JsonProperty("v")
  private BigDecimal volume;

  public CandleStick toEntity(TimeFrame interval) {
    return CandleStick.builder()
        .startTime(startTime)
        .endTime(startTime.plus(interval.getMin(), ChronoUnit.MINUTES))
        .open(open)
        .close(close)
        .high(high)
        .low(low)
        .timeFrame(interval)
        .build();
  }

}

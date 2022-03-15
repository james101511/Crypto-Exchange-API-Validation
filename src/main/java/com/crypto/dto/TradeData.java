package com.crypto.dto;

import com.crypto.config.LongToInstantConverter;
import com.crypto.entity.Trade;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TradeData {

  @JsonProperty("d")
  private String tradeId;
  @JsonProperty("s")
  private String side;
  @JsonProperty("p")
  private BigDecimal price;
  @JsonProperty("q")
  private BigDecimal quantity;
  @JsonProperty("t")
  @JsonDeserialize(converter = LongToInstantConverter.class)
  private Instant tradeTime;
  @JsonProperty("i")
  private String instrument;

  public Trade toEntity() {
    return Trade.builder()
        .instrument(instrument)
        .quantity(quantity)
        .tradeTime(tradeTime)
        .price(price)
        .build();
  }
}

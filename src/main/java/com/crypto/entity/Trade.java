package com.crypto.entity;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Trade {
  
  private BigDecimal price;
  private Instant tradeTime;
  private String instrument;
  private BigDecimal quantity;
}

package com.crypto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeFrame {
  ONE_MINUTE("1m", 1),
  FIVE_MINUTES("5m", 5),
  FIFTEEN_MINUTES("15m", 15),
  THIRTY_MINUTES("30m", 30),
  ONE_HOUR("1h", 60),
  FOUR_HOURS("4h", 60 * 4),
  SIX_HOURS("6h", 60 * 6),
  TWELVE_HOURS("12h", 60 * 12),
  ONE_DAY("1D", 60 * 24),
  ONE_WEEK("7D", 60 * 24 * 7),
  TWO_WEEKS("14D", 60 * 24 * 14),
  ONE_MONTH("1M", 60 * 24 * 30);

  @JsonValue
  private final String code;
  private final Integer min;

}

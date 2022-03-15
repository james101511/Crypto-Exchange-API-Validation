package com.crypto.dto;

import com.crypto.entity.Trade;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeResult {

  @JsonProperty("instrument_name")
  private String instrumentName;

  private List<TradeData> data;

  public List<Trade> getSortedData() {
    return Optional.ofNullable(data)
        .map(Collection::stream)
        .orElse(Stream.empty())
        .map(TradeData::toEntity)
        .sorted(Comparator.comparing(Trade::getTradeTime))
        .collect(Collectors.toList());
  }
}

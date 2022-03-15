package com.crypto.dto;

import com.crypto.entity.CandleStick;
import com.crypto.enums.TimeFrame;
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
@AllArgsConstructor
@NoArgsConstructor
public class CandleStickResult {

  @JsonProperty("instrument_name")
  private String instrumentName;

  private TimeFrame interval;

  private List<CandleStickData> data;

  public List<CandleStick> getSortedData() {
    return Optional.ofNullable(data)
        .map(Collection::stream)
        .orElse(Stream.empty())
        .map(data -> data.toEntity(interval))
        .sorted(Comparator.comparing(CandleStick::getEndTime))
        .collect(Collectors.toList());
  }
}

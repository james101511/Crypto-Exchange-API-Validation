package com.crypto.config;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.Instant;

public class LongToInstantConverter extends StdConverter<Long, Instant> {

  public Instant convert(final Long value) {
    return Instant.ofEpochMilli(value);
  }
}

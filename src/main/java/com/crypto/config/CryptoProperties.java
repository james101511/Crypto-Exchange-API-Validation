package com.crypto.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cdc")
@Data
public class CryptoProperties {

  private String url;

  public String getCandleStickUrl(String instrumentName, String timeframe) {
    return String.format(
        url + "/public/get-candlestick?instrument_name=%s&timeframe=%s", instrumentName, timeframe
    );
  }

  public String getTradeUrl(String instrumentName) {
    return String.format(url + "/public/get-trades?instrument_name=%s", instrumentName);
  }

}

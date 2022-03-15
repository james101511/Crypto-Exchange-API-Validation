package com.crypto;

import com.crypto.enums.TimeFrame;
import com.crypto.service.CryptoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {

  @Autowired
  private CryptoService cryptoService;

  @Test
  void runWithOneMinuteBTC() {
    cryptoService.startVerify("BTC_USDT", TimeFrame.ONE_MINUTE);
  }

  @Test
  void runWithFiveMinutesBTC() {
    cryptoService.startVerify("BTC_USDT", TimeFrame.FIVE_MINUTES);
  }

  @Test
  void runWithOneMinuteCRO() {
    cryptoService.startVerify("CRO_USDT", TimeFrame.ONE_MINUTE);
  }

  @Test
  void runWithFiveMinutesCRO() {
    cryptoService.startVerify("CRO_USDT", TimeFrame.FIVE_MINUTES);
  }


}

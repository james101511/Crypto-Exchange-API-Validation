# Crypto.com API Reconciliation

## Introduction

This project is aimed to verify the consistency between Candle Stick API and the output of Trade
API, developed in Java with JUnit5 unit test.

## Setup

* Java version requirement: JDK 11
* Installation
    ```
    $ mvn clean install or $ mvnw clean install
    ```

## Description

The project is based on the following services and methods:

1. The validation is started by **`CryptoService`**.
2. `separateTradesByCandleTime` method in  **`CandleService`** generates a map with candle and
   involved trades as key-value pairs.
3. `checkCandleValidBySortedTrades` method will help to check each trade if it meets the specific O,
   C, H, L price.

## To Be Confirmed

API document shows the response from candlesticks
**`t`** refers to the end time of candlestick (Unix timestamp).
![](https://i.imgur.com/kX56kVq.png)

![](https://i.imgur.com/aSEN5rt.png)

However, the result will be weird if **`t`** equals the end time since I found that the last
candstick's end time is earlier than the first trades most of the time, which means there will be no
overlaps. Also, the sample response of **`candlestick.{interval}.{instrument_name}`** in the
document shows that **`t`** is the start time. Thus, **`t`** is considered as the start time of each
trade in the project.

## Usage

```
mvn test 
```

Add or modify

* any time frame (from **`TimeFrame`** enumeration),
* trading pairs into **`ApplicationTest`**, which will help to log the result.

## Test Case

There are four classes, including different test cases with specific services by each.

* **`CandleServiceTest`**
  There are two functions in **`CandleSerive`**:
    1. Rearrange the trades according to the time sequence of candlestick.
    2. Check **`O`**, **`C`**, **`H`**, **`L`** are the same from specific trades.

* **`CryptoTest`**
  Aim to test some edge cases for **`CryptoSerive`**.

* **`FetchServiceTest`**
  **`Fetchservice`** is used to get the data from the API. **`FetchServiceTest`** is to check the
  service if it throws exceptions properly when errors occurred.

* **`ApplicationTest`**
  This class is for the integration test, including `cro-usdt` and `btc-usdt` with 1 and 5-minutes
  time frames each.

    ``` java
    @Test
    void runWithOneMinuteBTC() {
      cryptoService.startVerify("BTC_USDT", TimeFrame.ONE_MINUTE);
    }

    @Test
    void runWithFiveMinutesCRO() {
       cryptoService.startVerify("CRO_USDT", TimeFrame.FIVE_MINUTES);
    }
    ```

    ``` log
    candlestick from 2022-03-12T02:01:00Z to 2022-03-12T02:02:00Z has no trades
    candlestick from 2022-03-12T02:00:00Z to 2022-03-12T02:01:00Z has no trades
    candlestick from 2022-03-12T11:59:00Z to 2022-03-12T12:00:00Z is invalid with 51 trades
    candleStick has open: 0.38757, close: 0.3875, high: 0.38775, low: 0.38745
    realCandleStick has open: 0.38757, close: 0.38739, high: 0.38775, low: 0.38737
    candlestick from 2022-03-12T11:58:00Z to 2022-03-12T11:59:00Z is valid with 78 trades
    candlestick from 2022-03-12T11:57:00Z to 2022-03-12T11:58:00Z is invalid with 65 trades
    candleStick has open: 0.38743, close: 0.38771, high: 0.38771, low: 0.38721
    realCandleStick has open: 0.38756, close: 0.38771, high: 0.38771, low: 0.38721
    candlestick from 2022-03-12T11:56:00Z to 2022-03-12T11:57:00Z has no trades
    candlestick from 2022-03-12T11:55:00Z to 2022-03-12T11:56:00Z has no trades
    candlestick from 2022-03-12T11:54:00Z to 2022-03-12T11:55:00Z has no trades
    candlestick from 2022-03-12T11:53:00Z to 2022-03-12T11:54:00Z has no trades
     ```

  The results will be shown as above.

  There are different situations and corresponding log examples as the following:

    * No trades in the candle:
       ```log
       {startTime} to {endTime} has no trades` will be logged.
       ```

    * Valid:
        ``` log
       candlestick from 2022-03-12T11:58:00Z to 2022-03-12T11:59:00Z is valid with 78 trades
        ```
    * Invalid (while the price does not meet the candle):
        ``` log
       candlestick from 2022-03-12T11:57:00Z to 2022-03-12T11:58:00Z is invalid with 65 trades
       candleStick has open: 0.38743, close: 0.38771, high: 0.38771, low: 0.38721
       realCandleStick has open: 0.38756, close: 0.38771, high: 0.38771, low: 0.38721
       ```





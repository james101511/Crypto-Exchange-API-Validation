package com.crypto.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response<T> {

  private Integer code;

  private String method;

  private T result;
}

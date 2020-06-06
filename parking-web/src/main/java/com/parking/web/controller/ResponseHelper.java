package com.parking.web.controller;

import com.parking.web.response.Response;
import org.springframework.http.HttpStatus;

final class ResponseHelper {

  private ResponseHelper() {
  }

  static <T> Response<T> ok(T data) {
    return Response.<T>builder()
        .status(HttpStatus.OK.getReasonPhrase())
        .code(HttpStatus.OK.value())
        .data(data)
        .build();
  }
}

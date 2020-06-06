package com.parking.command.helper;

import java.util.Objects;

public final class PriceHelper {

  private PriceHelper() {
  }

  public static Long calculatePrice(Long duration) {
    if (Objects.isNull(duration)) {
      return null;
    }
    long price;
    if (duration <= 0) {
      price = 2000L;
    } else if (duration > 10) {
      price = 20000L;
    } else {
      price = duration * 2000;
    }
    return price;
  }
}

package com.parking.command.impl;

import com.parking.command.TimeService;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

  @Override
  public long nowMillis() {
    return System.currentTimeMillis();
  }
}

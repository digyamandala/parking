package com.parking.command.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

  @InjectMocks
  private TimeServiceImpl timeService;

  @Test
  void nowMillis() {
    Assertions.assertThat(timeService.nowMillis())
        .isNotNull();
  }
}

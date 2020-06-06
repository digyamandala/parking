package com.parking.command.impl;

import com.parking.command.TimeService;
import com.parking.command.model.GetParkingLotsCommandRequest;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.model.ParkingLot;
import com.parking.web.response.ParkingLotWebResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetParkingLotsCommandImplTest {

  private static final long CURRENT_MILLIS = 1591383600000L;

  private static final long IN_DATE = 1591376400000L;

  @InjectMocks
  private GetParkingLotsCommandImpl command;

  @Mock
  private ParkingLotRepository parkingLotRepository;

  @Mock
  private TimeService timeService;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(parkingLotRepository);
  }

  @Test
  void execute() {
    GetParkingLotsCommandRequest commandRequest = GetParkingLotsCommandRequest.builder()
        .occupied(null)
        .build();

    ParkingLot parkingLot = ParkingLot.builder()
        .occupied(true)
        .inDate(IN_DATE)
        .build();

    when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));

    when(timeService.nowMillis()).thenReturn(CURRENT_MILLIS);

    ParkingLotWebResponse expected = ParkingLotWebResponse.builder()
        .duration(2L)
        .occupied(true)
        .inDate(IN_DATE)
        .price(4000L)
        .build();

    List<ParkingLotWebResponse> response = command.execute(commandRequest);

    assertThat(response).contains(expected);
  }

  @Test
  void execute_withNotNullOccupied() {
    GetParkingLotsCommandRequest commandRequest = GetParkingLotsCommandRequest.builder()
        .occupied(true)
        .build();

    ParkingLot parkingLot = ParkingLot.builder()
        .occupied(true)
        .inDate(IN_DATE)
        .build();

    when(parkingLotRepository.findAllByOccupied(true)).thenReturn(Collections.singletonList(parkingLot));

    when(timeService.nowMillis()).thenReturn(CURRENT_MILLIS);

    ParkingLotWebResponse expected = ParkingLotWebResponse.builder()
        .duration(2L)
        .occupied(true)
        .inDate(IN_DATE)
        .price(4000L)
        .build();

    List<ParkingLotWebResponse> response = command.execute(commandRequest);

    assertThat(response)
        .contains(expected);
  }
}

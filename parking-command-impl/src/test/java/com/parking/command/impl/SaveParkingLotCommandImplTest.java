package com.parking.command.impl;

import com.parking.command.TimeService;
import com.parking.command.model.SaveParkingLotCommandRequest;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.model.ParkingLot;
import com.parking.web.response.ParkingLotWebResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveParkingLotCommandImplTest {

  private static final long IN_DATE = 8L;

  private static final String CAR_PLATE = "car-plate";

  @InjectMocks
  private SaveParkingLotCommandImpl command;

  @Mock
  private ParkingLotRepository parkingLotRepository;

  @Mock
  private TimeService timeService;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(parkingLotRepository, timeService);
  }

  @Test
  void execute() {
    SaveParkingLotCommandRequest commandRequest = SaveParkingLotCommandRequest.builder()
        .carPlate(CAR_PLATE)
        .build();

    ParkingLot parkingLot = ParkingLot.builder()
        .floor(0)
        .number(1)
        .build();

    when(parkingLotRepository.findFirstByCarPlateNull()).thenReturn(parkingLot);

    when(timeService.nowMillis()).thenReturn(IN_DATE);

    ParkingLot parkingLotAfterUpdate = ParkingLot.builder()
        .carPlate(CAR_PLATE)
        .floor(0)
        .number(1)
        .inDate(IN_DATE)
        .occupied(true)
        .build();

    when(parkingLotRepository.save(parkingLotAfterUpdate)).thenReturn(parkingLotAfterUpdate);

    ParkingLotWebResponse expected = ParkingLotWebResponse.builder()
        .carPlate(CAR_PLATE)
        .floor(0)
        .inDate(IN_DATE)
        .number(1)
        .build();

    ParkingLotWebResponse response = command.execute(commandRequest);

    assertThat(response).isEqualTo(expected);
  }
}

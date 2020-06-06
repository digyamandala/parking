package com.parking.command.impl;

import com.parking.command.TimeService;
import com.parking.command.model.DeleteParkingLotCommandRequest;
import com.parking.repository.ParkingLotHistoryRepository;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.model.ParkingLot;
import com.parking.repository.model.ParkingLotHistory;
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
class DeleteParkingLotCommandImplTest {

  private static final String PARKING_LOT_ID = "parking-lot-id";

  private static final String CAR_PLATE = "car-plate";

  private static final long IN_DATE = 1591378200000L;

  private static final long NOW = 1591385400000L;

  @InjectMocks
  private DeleteParkingLotCommandImpl command;

  @Mock
  private ParkingLotHistoryRepository parkingLotHistoryRepository;

  @Mock
  private ParkingLotRepository parkingLotRepository;

  @Mock
  private TimeService timeService;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(parkingLotHistoryRepository, parkingLotRepository, timeService);
  }

  @Test
  void execute() {
    DeleteParkingLotCommandRequest commandRequest = DeleteParkingLotCommandRequest.builder()
        .parkingLotId(PARKING_LOT_ID)
        .build();

    ParkingLot parkingLot = ParkingLot.builder()
        .inDate(IN_DATE)
        .carPlate(CAR_PLATE)
        .occupied(true)
        .build();

    when(parkingLotRepository.findByIdAndOccupied(PARKING_LOT_ID, true)).thenReturn(parkingLot);

    ParkingLot updatedParkingLot = ParkingLot.builder()
        .occupied(false)
        .carPlate(null)
        .inDate(null)
        .build();

    when(parkingLotRepository.save(updatedParkingLot)).thenReturn(updatedParkingLot);

    when(timeService.nowMillis()).thenReturn(NOW);

    ParkingLotHistory parkingLotHistory = ParkingLotHistory.builder()
        .outDate(NOW)
        .price(4000L)
        .duration(2L)
        .inDate(IN_DATE)
        .carPlate(CAR_PLATE)
        .build();

    when(parkingLotHistoryRepository.save(parkingLotHistory)).thenReturn(parkingLotHistory);

    ParkingLotWebResponse expected = ParkingLotWebResponse.builder()
        .duration(2L)
        .carPlate(CAR_PLATE)
        .price(4000L)
        .inDate(IN_DATE)
        .outDate(NOW)
        .build();

    ParkingLotWebResponse response = command.execute(commandRequest);

    assertThat(response).isEqualTo(expected);
  }
}

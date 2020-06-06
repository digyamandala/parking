package com.parking.command.impl;

import com.parking.command.SaveParkingLotCommand;
import com.parking.command.TimeService;
import com.parking.command.model.SaveParkingLotCommandRequest;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.model.ParkingLot;
import com.parking.web.response.ParkingLotWebResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaveParkingLotCommandImpl implements SaveParkingLotCommand {

  private final ParkingLotRepository parkingLotRepository;

  private final TimeService timeService;

  public SaveParkingLotCommandImpl(ParkingLotRepository parkingLotRepository,
      TimeService timeService) {
    this.parkingLotRepository = parkingLotRepository;
    this.timeService = timeService;
  }

  @Override
  public ParkingLotWebResponse execute(SaveParkingLotCommandRequest commandRequest) {
    return Optional.ofNullable(commandRequest)
        .map(commandRequest1 -> findNearestEmptyLot())
        .map(parkingLot -> updateParkingLot(commandRequest, parkingLot))
        .map(parkingLotRepository::save)
        .map(this::toWebResponse)
        .orElseGet(ParkingLotWebResponse::new);
  }

  private ParkingLot findNearestEmptyLot() {
    return parkingLotRepository.findFirstByCarPlateNull();
  }

  private ParkingLotWebResponse toWebResponse(ParkingLot parkingLot) {
    return ParkingLotWebResponse.builder()
        .carPlate(parkingLot.getCarPlate())
        .floor(parkingLot.getFloor())
        .inDate(parkingLot.getInDate())
        .number(parkingLot.getNumber())
        .build();
  }

  private ParkingLot updateParkingLot(SaveParkingLotCommandRequest commandRequest, ParkingLot parkingLot) {
    parkingLot.setCarPlate(commandRequest.getCarPlate());
    parkingLot.setInDate(timeService.nowMillis());
    parkingLot.setOccupied(true);
    return parkingLot;
  }
}

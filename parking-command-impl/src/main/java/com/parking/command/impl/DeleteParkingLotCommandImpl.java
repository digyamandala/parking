package com.parking.command.impl;

import com.parking.command.DeleteParkingLotCommand;
import com.parking.command.TimeService;
import com.parking.command.helper.PriceHelper;
import com.parking.command.model.DeleteParkingLotCommandRequest;
import com.parking.repository.ParkingLotHistoryRepository;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.model.ParkingLot;
import com.parking.repository.model.ParkingLotHistory;
import com.parking.web.response.ParkingLotWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class DeleteParkingLotCommandImpl implements DeleteParkingLotCommand {

  private final ParkingLotHistoryRepository parkingLotHistoryRepository;

  private final ParkingLotRepository parkingLotRepository;

  private final TimeService timeService;

  public DeleteParkingLotCommandImpl(ParkingLotHistoryRepository parkingLotHistoryRepository,
      ParkingLotRepository parkingLotRepository, TimeService timeService) {
    this.parkingLotHistoryRepository = parkingLotHistoryRepository;
    this.parkingLotRepository = parkingLotRepository;
    this.timeService = timeService;
  }

  @Override
  public ParkingLotWebResponse execute(DeleteParkingLotCommandRequest commandRequest) {
    return Optional.ofNullable(commandRequest)
        .map(this::findParkingLot)
        .map(this::emptyLot)
        .map(this::toParkingLotHistory)
        .map(parkingLotHistoryRepository::save)
        .map(this::toWebResponse)
        .orElse(null);
  }

  private ParkingLot findParkingLot(DeleteParkingLotCommandRequest commandRequest) {
    return parkingLotRepository.findByIdAndOccupied(commandRequest.getParkingLotId(), true);
  }

  private ParkingLotWebResponse toWebResponse(ParkingLotHistory parkingLotHistory) {
    return ParkingLotWebResponse.builder()
        .carPlate(parkingLotHistory.getCarPlate())
        .duration(parkingLotHistory.getDuration())
        .floor(parkingLotHistory.getFloor())
        .inDate(parkingLotHistory.getInDate())
        .number(parkingLotHistory.getNumber())
        .outDate(parkingLotHistory.getOutDate())
        .price(parkingLotHistory.getPrice())
        .build();
  }

  private ParkingLotHistory toParkingLotHistory(ParkingLot parkingLot) {
    long outDate = timeService.nowMillis();
    long duration = getHoursDuration(parkingLot.getInDate(), outDate);
    return ParkingLotHistory.builder()
        .carPlate(parkingLot.getCarPlate())
        .duration(duration)
        .floor(parkingLot.getFloor())
        .inDate(parkingLot.getInDate())
        .number(parkingLot.getNumber())
        .outDate(outDate)
        .price(PriceHelper.calculatePrice(duration))
        .build();
  }

  private long getHoursDuration(long inDate, long outDate) {
    return TimeUnit.MILLISECONDS.toHours(outDate - inDate);
  }

  private ParkingLot emptyLot(ParkingLot parkingLot) {

    ParkingLot parkingLotCopy = new ParkingLot();

    Optional.of(parkingLot)
        .ifPresent(parkingLot1 -> BeanUtils.copyProperties(parkingLot, parkingLotCopy));

    Optional.of(parkingLot)
        .map(this::resetParkingLot)
        .map(parkingLotRepository::save);

    return parkingLotCopy;
  }

  private ParkingLot resetParkingLot(ParkingLot parkingLot1) {
    parkingLot1.setCarPlate(null);
    parkingLot1.setInDate(null);
    parkingLot1.setOccupied(false);
    return parkingLot1;
  }
}

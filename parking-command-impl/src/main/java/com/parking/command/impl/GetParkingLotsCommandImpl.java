package com.parking.command.impl;

import com.parking.command.GetParkingLotsCommand;
import com.parking.command.TimeService;
import com.parking.command.helper.PriceHelper;
import com.parking.command.model.GetParkingLotsCommandRequest;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.model.ParkingLot;
import com.parking.web.response.ParkingLotWebResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GetParkingLotsCommandImpl implements GetParkingLotsCommand {

  private final ParkingLotRepository parkingLotRepository;

  private final TimeService timeService;

  public GetParkingLotsCommandImpl(ParkingLotRepository parkingLotRepository,
      TimeService timeService) {
    this.parkingLotRepository = parkingLotRepository;
    this.timeService = timeService;
  }

  @Override
  public List<ParkingLotWebResponse> execute(GetParkingLotsCommandRequest commandRequest) {
    return Optional.ofNullable(commandRequest)
        .map(GetParkingLotsCommandRequest::getOccupied)
        .map(parkingLotRepository::findAllByOccupied)
        .map(this::toWebResponses)
        .orElseGet(this::findAll);
  }

  private List<ParkingLotWebResponse> findAll() {
    return parkingLotRepository.findAll()
        .stream()
        .map(this::toWebResponse)
        .collect(Collectors.toList());
  }

  private List<ParkingLotWebResponse> toWebResponses(List<ParkingLot> parkingLots) {
    return parkingLots.stream()
        .map(this::toWebResponse)
        .collect(Collectors.toList());
  }

  private ParkingLotWebResponse toWebResponse(ParkingLot parkingLot) {
    long current = timeService.nowMillis();
    Long duration = getDuration(parkingLot.getInDate(), current);
    return ParkingLotWebResponse.builder()
        .carPlate(parkingLot.getCarPlate())
        .duration(duration)
        .floor(parkingLot.getFloor())
        .inDate(parkingLot.getInDate())
        .number(parkingLot.getNumber())
        .price(PriceHelper.calculatePrice(duration))
        .occupied(parkingLot.isOccupied())
        .build();
  }

  private Long getDuration(Long inDate, long current) {
    return Optional.ofNullable(inDate)
        .map(in -> TimeUnit.MILLISECONDS.toHours(current - inDate))
        .orElse(null);
  }
}

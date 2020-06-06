package com.parking.command.impl;

import com.parking.command.InitializeParkingLotsCommand;
import com.parking.command.model.InitializeParkingLotsCommandRequest;
import com.parking.repository.ParkingLotRepository;
import com.parking.repository.model.ParkingLot;
import com.parking.web.response.ParkingLotWebResponse;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class InitializeParkingLotsCommandImpl implements InitializeParkingLotsCommand {

  private final ParkingLotRepository parkingLotRepository;

  public InitializeParkingLotsCommandImpl(ParkingLotRepository parkingLotRepository) {
    this.parkingLotRepository = parkingLotRepository;
  }

  @Override
  public List<ParkingLotWebResponse> execute(InitializeParkingLotsCommandRequest commandRequest) {
    return Optional.ofNullable(commandRequest)
        .map(commandRequest1 -> initializeParkingLots())
        .map(parkingLotRepository::saveAll)
        .map(parkingLots -> parkingLots.stream()
            .map(this::toWebResponse)
            .collect(Collectors.toList()))
        .orElseGet(Collections::emptyList);
  }

  private List<ParkingLot> initializeParkingLots() {
    return IntStream.range(0, 1)
        .mapToObj(floor -> IntStream.range(0, 20)
            .mapToObj(number -> toParkingLot(floor, number))
            .collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  private ParkingLot toParkingLot(int floor, int number) {
    return ParkingLot.builder()
        .floor(floor)
        .number(number)
        .build();
  }

  private ParkingLotWebResponse toWebResponse(ParkingLot parkingLot) {
    return ParkingLotWebResponse.builder()
        .floor(parkingLot.getFloor())
        .number(parkingLot.getNumber())
        .build();
  }
}

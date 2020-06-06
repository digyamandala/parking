package com.parking.repository;

import com.parking.repository.model.ParkingLot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ParkingLotRepository extends MongoRepository<ParkingLot, String> {

  ParkingLot findFirstByCarPlateNull();

  ParkingLot findByIdAndOccupied(String id, boolean occupied);

  List<ParkingLot> findAllByOccupied(boolean occupied);
}

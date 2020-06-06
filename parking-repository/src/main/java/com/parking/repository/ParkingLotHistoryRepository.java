package com.parking.repository;

import com.parking.repository.model.ParkingLotHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParkingLotHistoryRepository extends MongoRepository<ParkingLotHistory, String> {

}

package com.parking.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "parking_lots")
public class ParkingLot {

  private String carPlate;

  private int floor;

  @Id
  private String id;

  private Long inDate;

  private int number;

  private boolean occupied;
}

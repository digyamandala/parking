package com.parking.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLotWebResponse {

  private String carPlate;

  private Long duration;

  private int floor;

  private Long inDate;

  private int number;

  private boolean occupied;

  private Boolean out;

  private Long outDate;

  private Long price;
}

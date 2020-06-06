package com.parking.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLotHistoryWebResponse {

  private String carPlate;

  private Long duration;

  private int floor;

  private Long inDate;

  private int number;

  private Long outDate;

  private long price;
}

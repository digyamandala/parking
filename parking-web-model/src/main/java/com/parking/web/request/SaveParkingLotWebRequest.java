package com.parking.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveParkingLotWebRequest {

  private String carColor;

  private String carManufacturer;

  private String carPlate;

  private String carType;

  private int floor;

  private int number;

  private boolean out;

}

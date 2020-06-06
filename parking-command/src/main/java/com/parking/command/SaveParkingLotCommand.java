package com.parking.command;

import com.parking.command.model.SaveParkingLotCommandRequest;
import com.parking.web.response.ParkingLotWebResponse;

public interface SaveParkingLotCommand extends Command<SaveParkingLotCommandRequest, ParkingLotWebResponse> {

}

package com.parking.command;

import com.parking.command.model.DeleteParkingLotCommandRequest;
import com.parking.web.response.ParkingLotWebResponse;

public interface DeleteParkingLotCommand extends Command<DeleteParkingLotCommandRequest, ParkingLotWebResponse> {

}

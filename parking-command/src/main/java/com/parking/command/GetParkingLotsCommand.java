package com.parking.command;

import com.parking.command.model.GetParkingLotsCommandRequest;
import com.parking.web.response.ParkingLotWebResponse;

import java.util.List;

public interface GetParkingLotsCommand extends
    Command<GetParkingLotsCommandRequest, List<ParkingLotWebResponse>> {

}

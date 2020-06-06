package com.parking.command;

import com.parking.command.model.InitializeParkingLotsCommandRequest;
import com.parking.web.response.ParkingLotWebResponse;

import java.util.List;

public interface InitializeParkingLotsCommand extends
    Command<InitializeParkingLotsCommandRequest, List<ParkingLotWebResponse>> {

}

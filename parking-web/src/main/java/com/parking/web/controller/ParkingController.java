package com.parking.web.controller;

import com.parking.command.DeleteParkingLotCommand;
import com.parking.command.GetParkingLotsCommand;
import com.parking.command.InitializeParkingLotsCommand;
import com.parking.command.SaveParkingLotCommand;
import com.parking.command.model.DeleteParkingLotCommandRequest;
import com.parking.command.model.GetParkingLotsCommandRequest;
import com.parking.command.model.InitializeParkingLotsCommandRequest;
import com.parking.command.model.SaveParkingLotCommandRequest;
import com.parking.web.request.DeleteParkingLotWebRequest;
import com.parking.web.request.SaveParkingLotWebRequest;
import com.parking.web.response.ParkingLotWebResponse;
import com.parking.web.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/backend/parking", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParkingController {

  private final SaveParkingLotCommand saveParkingLotCommand;

  private final DeleteParkingLotCommand deleteParkingLotCommand;

  private final GetParkingLotsCommand getParkingLotsCommand;

  private final InitializeParkingLotsCommand initializeParkingLotsCommand;

  @Autowired
  public ParkingController(SaveParkingLotCommand saveParkingLotCommand,
      DeleteParkingLotCommand deleteParkingLotCommand,
      GetParkingLotsCommand getParkingLotsCommand,
      InitializeParkingLotsCommand initializeParkingLotsCommand) {
    this.saveParkingLotCommand = saveParkingLotCommand;
    this.deleteParkingLotCommand = deleteParkingLotCommand;
    this.getParkingLotsCommand = getParkingLotsCommand;
    this.initializeParkingLotsCommand = initializeParkingLotsCommand;
  }

  @GetMapping
  public Response<List<ParkingLotWebResponse>> getParkingLots(@RequestParam(required = false) Boolean occupied) {
    GetParkingLotsCommandRequest commandRequest = GetParkingLotsCommandRequest.builder()
        .occupied(occupied)
        .build();

    return ResponseHelper.ok(getParkingLotsCommand.execute(commandRequest));
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public Response<ParkingLotWebResponse> parkIn(@RequestBody SaveParkingLotWebRequest webRequest) {
    SaveParkingLotCommandRequest commandRequest = SaveParkingLotCommandRequest.builder()
        .carPlate(webRequest.getCarPlate())
        .build();

    return ResponseHelper.ok(saveParkingLotCommand.execute(commandRequest));
  }

  @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public Response<ParkingLotWebResponse> parkOut(@RequestBody DeleteParkingLotWebRequest webRequest) {
    DeleteParkingLotCommandRequest commandRequest = DeleteParkingLotCommandRequest.builder()
        .parkingLotId(webRequest.getParkingLotId())
        .build();

    return ResponseHelper.ok(deleteParkingLotCommand.execute(commandRequest));
  }

  @PostMapping(path = "/init", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Response<List<ParkingLotWebResponse>> initialize() {
    return ResponseHelper.ok(initializeParkingLotsCommand.execute(new InitializeParkingLotsCommandRequest()));
  }
}

package com.parking.web.controller;

import com.parking.command.DeleteParkingLotCommand;
import com.parking.command.GetParkingLotsCommand;
import com.parking.command.InitializeParkingLotsCommand;
import com.parking.command.SaveParkingLotCommand;
import com.parking.command.model.DeleteParkingLotCommandRequest;
import com.parking.command.model.GetParkingLotsCommandRequest;
import com.parking.command.model.InitializeParkingLotsCommandRequest;
import com.parking.command.model.SaveParkingLotCommandRequest;
import com.parking.web.response.ParkingLotWebResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ParkingController.class)
@AutoConfigureMockMvc
class ParkingControllerTest {

  private static final String PARKING_LOT_ID = "parking-lot-id";

  private static final String CAR_PLATE = "car-plate";

  @MockBean
  private DeleteParkingLotCommand deleteParkingLotCommand;

  @MockBean
  private GetParkingLotsCommand getParkingLotsCommand;

  @MockBean
  private InitializeParkingLotsCommand initializeParkingLotsCommand;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SaveParkingLotCommand saveParkingLotCommand;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(deleteParkingLotCommand, getParkingLotsCommand, initializeParkingLotsCommand,
        saveParkingLotCommand);
  }

  @Test
  void getParkingLots() throws Exception {

    GetParkingLotsCommandRequest commandRequest = GetParkingLotsCommandRequest.builder()
        .occupied(true)
        .build();

    when(getParkingLotsCommand.execute(commandRequest)).thenReturn(
        Collections.singletonList(ParkingLotWebResponse.builder()
            .carPlate(CAR_PLATE)
            .build()));

    mockMvc.perform(MockMvcRequestBuilders.get("/backend/parking")
        .queryParam("occupied", "true"))
        .andExpect(MockMvcResultMatchers.status()
            .isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].carPlate", Matchers.is(CAR_PLATE)));

    verify(getParkingLotsCommand).execute(commandRequest);
  }

  @Test
  void parkIn() throws Exception {
    SaveParkingLotCommandRequest commandRequest = SaveParkingLotCommandRequest.builder()
        .carPlate(CAR_PLATE)
        .build();

    when(saveParkingLotCommand.execute(commandRequest)).thenReturn(ParkingLotWebResponse.builder()
        .carPlate(CAR_PLATE)
        .build());

    mockMvc.perform(MockMvcRequestBuilders.post("/backend/parking")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .content("{\"carPlate\":\"car-plate\"}"))
        .andExpect(MockMvcResultMatchers.status()
            .isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.carPlate", Matchers.is(CAR_PLATE)));

    verify(saveParkingLotCommand).execute(commandRequest);
  }

  @Test
  void parkOut() throws Exception {
    DeleteParkingLotCommandRequest commandRequest = DeleteParkingLotCommandRequest.builder()
        .parkingLotId(PARKING_LOT_ID)
        .build();

    when(deleteParkingLotCommand.execute(commandRequest)).thenReturn(ParkingLotWebResponse.builder()
        .carPlate(CAR_PLATE)
        .build());

    mockMvc.perform(MockMvcRequestBuilders.delete("/backend/parking")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("{\"parkingLotId\":\"parking-lot-id\"}"))
        .andExpect(MockMvcResultMatchers.status()
            .isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.carPlate", Matchers.is(CAR_PLATE)));

    verify(deleteParkingLotCommand).execute(commandRequest);
  }

  @Test
  void initialize() throws Exception {
    InitializeParkingLotsCommandRequest commandRequest = InitializeParkingLotsCommandRequest.builder()
        .build();

    when(initializeParkingLotsCommand.execute(commandRequest)).thenReturn(Collections.singletonList(
        ParkingLotWebResponse.builder()
            .number(3)
            .build()));

    mockMvc.perform(MockMvcRequestBuilders.post("/backend/parking/init")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("{}"))
        .andExpect(MockMvcResultMatchers.status()
            .isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].number", Matchers.is(3)));

    verify(initializeParkingLotsCommand).execute(commandRequest);
  }
}

package com.bookmyshow.controller;

import com.bookmyshow.dto.SeatRequestDTO;
import com.bookmyshow.model.Seat;
import com.bookmyshow.model.Show;
import com.bookmyshow.service.SeatService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(SeatController.class)
@Import(com.bookmyshow.config.SpringSecurityConfig.class)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @Autowired
    private ObjectMapper objectMapper;

    private Seat seat;
    private SeatRequestDTO seatRequest;
    private Show show;

    @BeforeEach
    void setUp() {
        show = new Show();
        show.setId(101L);

        seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("A1");
        seat.setIsBooked(true);
        seat.setShow(show);


        seatRequest = new SeatRequestDTO();
        seatRequest.setSeatNumber("A1");
        seatRequest.setIsBooked(false);
        seatRequest.setShowId(101L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetSeatsByShow() throws Exception {
        Long showId = 101L;
        List<Seat> seats = List.of(seat);
        Mockito.when(seatService.getSeatsByShow(showId)).thenReturn(seats);
        mockMvc.perform(get("/api/seats/show/{showId}", showId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].seatNumber").value("A1"))
                .andExpect(jsonPath("$[0].isBooked").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetSeatById() throws Exception {
        when(seatService.getSeatById(1L)).thenReturn(seat);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/seats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value("A1"));

        verify(seatService, times(1)).getSeatById(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddSeat() throws Exception {
        when(seatService.createSeat(any(SeatRequestDTO.class))).thenReturn(seat);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seatRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value("A1"));

        verify(seatService, times(1)).createSeat(any(SeatRequestDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteSeat() throws Exception {
        doNothing().when(seatService).deleteSeat(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/seats/1"))
                .andExpect(status().isOk());

        verify(seatService, times(1)).deleteSeat(1L);
    }

    @Test
    @WithMockUser(username = "pavithra", roles = {"USER"})
    public void testAccessDeniedForUserRole() throws Exception {
        mockMvc.perform(get("/api/seats"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateSeatCase() throws Exception {
        Long seatId = 1L;

        SeatRequestDTO seatRequestDTO = new SeatRequestDTO();
        seatRequestDTO.setSeatNumber("A12");
        seatRequestDTO.setIsBooked(true);

        Seat updatedSeat = new Seat();
        updatedSeat.setId(seatId);
        updatedSeat.setSeatNumber("A12");
        updatedSeat.setIsBooked(true);

        Mockito.when(seatService.updateSeat(eq(seatId), any(SeatRequestDTO.class)))
                .thenReturn(updatedSeat);

        mockMvc.perform(put("/api/seats/update/{id}", seatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seatRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(seatId))
                .andExpect(jsonPath("$.seatNumber").value("A12"))
                .andExpect(jsonPath("$.isBooked").value(true));
    }
}

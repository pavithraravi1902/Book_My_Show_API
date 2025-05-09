package com.bookmyshow.controller;

import com.bookmyshow.config.SpringSecurityConfig;
import com.bookmyshow.dto.BookingSeatRequest;
import com.bookmyshow.model.BookingSeat;
import com.bookmyshow.service.BookingSeatService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingSeatController.class)
@Import(SpringSecurityConfig.class)
public class BookingSeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingSeatService bookingSeatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateBookingSeat() throws Exception {
        BookingSeatRequest request = new BookingSeatRequest();
        BookingSeat mockResponse = new BookingSeat();
        mockResponse.setId(1L);
        Mockito.when(bookingSeatService.saveBookingSeat(Mockito.any())).thenReturn(mockResponse);
        mockMvc.perform(post("/api/booking-seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllBookingSeats() throws Exception {
        BookingSeat seat = new BookingSeat();
        seat.setId(1L);

        Mockito.when(bookingSeatService.getAllBookingSeats()).thenReturn(Collections.singletonList(seat));

        mockMvc.perform(get("/api/booking-seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteBookingSeat() throws Exception {
        Mockito.doNothing().when(bookingSeatService).deleteBookingSeat(1L);
        mockMvc.perform(delete("/api/booking-seats/1"))
                .andExpect(status().isOk());
    }
}

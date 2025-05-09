package com.bookmyshow.controller;

import com.bookmyshow.config.SpringSecurityConfig;
import com.bookmyshow.dto.BookingRequest;
import com.bookmyshow.model.Booking;
import com.bookmyshow.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@Import(SpringSecurityConfig.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingRequest bookingRequest;
    private Booking booking;

    @BeforeEach
    void setUp() {
        bookingRequest = new BookingRequest();
        bookingRequest.setUserId(1L);
        bookingRequest.setShowId(100L);
        bookingRequest.setBookingTime(LocalDateTime.now());
        bookingRequest.setTotalAmount(new BigDecimal("1000.00"));

        booking = new Booking();
        booking.setId(1L);


        booking.setBookingTime(bookingRequest.getBookingTime());
        booking.setTotalAmount(bookingRequest.getTotalAmount());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateBooking() throws Exception {
        when(bookingService.saveBooking(any(BookingRequest.class))).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllBookings() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(booking);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteBooking() throws Exception {
        doNothing().when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).deleteBooking(1L);
    }
}

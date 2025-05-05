package com.bookmyshow.controller;

import com.bookmyshow.dto.SeatRequestDTO;
import com.bookmyshow.model.Seat;
import com.bookmyshow.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "http://localhost:3000")
public class SeatController {

    @Autowired
    private SeatService seatService;

    // Get all seats by show ID
    @GetMapping("/show/{showId}")
    public List<Seat> getSeatsByShow(@PathVariable Long showId) {
        return seatService.getSeatsByShow(showId);
    }

    // Get seat by seat ID
    @GetMapping("/{id}")
    public Seat getSeatById(@PathVariable Long id) {
        return seatService.getSeatById(id);
    }

    // Book a seat (mark as booked)
    @PutMapping("/book/{id}")
    public Seat bookSeat(@PathVariable Long id) {
        return seatService.bookSeat(id);
    }

    // Add a new seat (requires Seat details)
    @PostMapping
    public Seat addSeat(@RequestBody SeatRequestDTO seat) {
        return seatService.createSeat(seat);
    }

    @PutMapping("/update/{id}")
    public Seat updateSeat(@PathVariable Long id, @RequestBody SeatRequestDTO seatRequest) {
        return seatService.updateSeat(id, seatRequest);
    }

    // Delete a seat by ID
    @DeleteMapping("/{id}")
    public void deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
    }
}

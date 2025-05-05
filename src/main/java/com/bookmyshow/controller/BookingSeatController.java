package com.bookmyshow.controller;

import com.bookmyshow.dto.BookingSeatRequest;
import com.bookmyshow.model.BookingSeat;
import com.bookmyshow.service.BookingSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking-seats")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingSeatController {

    @Autowired
    private BookingSeatService bookingSeatService;

    @PostMapping
    public BookingSeat createBookingSeat(@RequestBody BookingSeatRequest bookingSeat) {
        return bookingSeatService.saveBookingSeat(bookingSeat);
    }

    @GetMapping
    public List<BookingSeat> getAllBookingSeats() {
        return bookingSeatService.getAllBookingSeats();
    }

    @DeleteMapping("/{id}")
    public void deleteBookingSeat(@PathVariable Long id) {
        bookingSeatService.deleteBookingSeat(id);
    }
}

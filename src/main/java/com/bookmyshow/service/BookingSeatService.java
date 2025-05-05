package com.bookmyshow.service;

import com.bookmyshow.dto.BookingSeatRequest;
import com.bookmyshow.dto.ShowRequestDTO;
import com.bookmyshow.model.*;
import com.bookmyshow.repository.BookingRepository;
import com.bookmyshow.repository.BookingSeatRepository;
import com.bookmyshow.repository.SeatRepository;
import com.bookmyshow.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingSeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingSeatRepository bookingSeatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public BookingSeat saveBookingSeat(BookingSeatRequest bookingSeatRequest) {
        Seat seat = seatRepository.findById(bookingSeatRequest.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found with ID: " + bookingSeatRequest.getSeatId()));

        Booking booking = bookingRepository.findById(bookingSeatRequest.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingSeatRequest.getBookingId()));

        BookingSeat bookingSeat = new BookingSeat();
        bookingSeat.setSeat(seat);
        bookingSeat.setBooking(booking);

        return bookingSeatRepository.save(bookingSeat);
    }

    public List<BookingSeat> getAllBookingSeats() {
        return bookingSeatRepository.findAll();
    }

    public void deleteBookingSeat(Long id) {
        bookingSeatRepository.deleteById(id);
    }
}

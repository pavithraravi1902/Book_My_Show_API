package com.bookmyshow.service;

import com.bookmyshow.dto.BookingRequest;
import com.bookmyshow.model.*;
import com.bookmyshow.repository.BookingRepository;
import com.bookmyshow.repository.SeatRepository;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeatRepository seatRepository;


    public Booking saveBooking(BookingRequest bookingRequest) {
        Show show = showRepository.findById(bookingRequest.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + bookingRequest.getShowId()));

        User user = userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + bookingRequest.getUserId()));

        Booking booking = new Booking();
        booking.setBookingTime(LocalDateTime.now());
        booking.setTotalAmount(bookingRequest.getTotalAmount());
        booking.setShow(show);
        booking.setUser(user);

        return bookingRepository.save(booking);
    }


    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        existingBooking.setBookingTime(updatedBooking.getBookingTime());
        existingBooking.setTotalAmount(updatedBooking.getTotalAmount());
        existingBooking.setUser(updatedBooking.getUser());
        existingBooking.setShow(updatedBooking.getShow());
        existingBooking.setBookingSeats(updatedBooking.getBookingSeats());

        return bookingRepository.save(existingBooking);
    }
}

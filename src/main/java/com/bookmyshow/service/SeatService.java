package com.bookmyshow.service;

import com.bookmyshow.dto.SeatRequestDTO;
import com.bookmyshow.model.Seat;
import com.bookmyshow.model.Show;
import com.bookmyshow.repository.SeatRepository;
import com.bookmyshow.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowRepository showRepository;

    // Get all seats (optionally filtered by showId if needed)
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public List<Seat> getSeatsByShow(Long showId) {
        return seatRepository.findByShowId(showId);
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id).orElseThrow(() -> new RuntimeException("Seat not found"));
    }

    public Seat createSeat(SeatRequestDTO seatRequest) {
        Show show = showRepository.findById(seatRequest.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + seatRequest.getShowId()));
        Seat seat = new Seat();
        seat.setSeatNumber(seatRequest.getSeatNumber());
        seat.setIsBooked(seatRequest.getIsBooked() != null ? seatRequest.getIsBooked() : false);
        seat.setShow(show);
        return seatRepository.save(seat);
    }


    public Seat updateSeat(Long id, SeatRequestDTO updatedSeat) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with ID: " + id));

        Show show = showRepository.findById(updatedSeat.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + updatedSeat.getShowId()));

        seat.setSeatNumber(updatedSeat.getSeatNumber());
        seat.setIsBooked(updatedSeat.getIsBooked() != null ? updatedSeat.getIsBooked() : false);
        seat.setShow(show);

        return seatRepository.save(seat);
    }


    public void deleteSeat(Long id) {
        seatRepository.deleteById(id);
    }

    public Seat bookSeat(Long id) {
        Seat seat = seatRepository.findById(id).orElseThrow(() -> new RuntimeException("Seat not found"));
        if (Boolean.TRUE.equals(seat.getIsBooked())) {
            throw new RuntimeException("Seat is already booked");
        }
        seat.setIsBooked(true);
        return seatRepository.save(seat);
    }
}

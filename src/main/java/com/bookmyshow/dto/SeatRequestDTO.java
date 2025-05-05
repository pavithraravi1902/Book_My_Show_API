package com.bookmyshow.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatRequestDTO {
    private String seatNumber;
    private Boolean isBooked;
    private Long showId;
}

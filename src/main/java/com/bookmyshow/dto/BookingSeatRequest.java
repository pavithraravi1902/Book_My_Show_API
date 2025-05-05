package com.bookmyshow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingSeatRequest {
    private Long seatId;
    private Long bookingId;
}

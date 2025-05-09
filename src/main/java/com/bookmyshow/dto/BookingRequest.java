package com.bookmyshow.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class BookingRequest {
    private LocalDateTime bookingTime = LocalDateTime.now();
    private BigDecimal totalAmount;
    private Long userId;
    private Long showId;
}

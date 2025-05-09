package com.bookmyshow.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShowRequestDTO {
    private Long movieId;
    private Long theatreId;
    private LocalDateTime showTime;
    private BigDecimal price;
}

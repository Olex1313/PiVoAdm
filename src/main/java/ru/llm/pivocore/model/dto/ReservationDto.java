package ru.llm.pivocore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long restaurantId;
    private Instant startReservationTime;
    private Instant endReservationTime;
    private Integer deposit;
    private Integer amountOfGuests;
}

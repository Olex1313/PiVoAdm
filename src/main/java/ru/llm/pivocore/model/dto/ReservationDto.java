package ru.llm.pivocore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReservationDto {
    @JsonProperty("restaurant_id")
    private Long restaurantId;
    @JsonProperty("start_reservation_time")
    private Instant startReservationTime;
    @JsonProperty("end_reservation_time")
    private Instant endReservationTime;
    @JsonProperty("deposit")
    private Integer deposit;
    @JsonProperty("amount_of_guests")
    private Short amountOfGuests;
}

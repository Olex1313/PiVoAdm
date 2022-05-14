package ru.llm.pivocore.model.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationRequest {

    @JsonProperty("restaurant_id")
    private Long restaurantId;

    @JsonProperty("start_reservation_time")
    private Instant startReservationTime;

    @JsonProperty("end_reservation_time")
    private Instant endReservationTime;

    @JsonProperty("deposit")
    private Integer deposit;

    @JsonProperty("amount_of_guests")
    private Integer amountOfGuests;
}

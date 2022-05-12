package ru.llm.pivocore.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationRequest {
    @JsonProperty("deposit")
    private Integer deposit;
    @JsonProperty("amount_of_guests")
    private Integer amountOfGuests;
}

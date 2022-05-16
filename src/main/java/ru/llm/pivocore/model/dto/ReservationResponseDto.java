package ru.llm.pivocore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ReservationResponseDto {
    @JsonProperty("reservation_id")
    private Long reservationId;
    @JsonProperty("is_approved")
    private Boolean isApproved;
}

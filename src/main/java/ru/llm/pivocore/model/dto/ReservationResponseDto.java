package ru.llm.pivocore.model.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReservationResponseDto {
    private Long reservationId;
    private Boolean isApproved;
}

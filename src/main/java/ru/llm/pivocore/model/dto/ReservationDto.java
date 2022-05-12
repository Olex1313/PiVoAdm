package ru.llm.pivocore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReservationDto {
    private LocalDate time;
    private Integer deposit;
    private Integer amountOfGuests;

}

package ru.llm.pivocore.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.dto.ReservationResponseDto;
import ru.llm.pivocore.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping(value = "/")
    public @ResponseBody List<ReservationDto> getAllCurrentReservations(@RequestParam Long restaurantId) {
        return reservationService.getAllReservations(restaurantId);
    }

    @PostMapping(value = "/approve")
    public @ResponseBody ReservationResponseDto submitReservation(@RequestParam Long reservationId) {
        return reservationService.approveById(reservationId);
    }
}

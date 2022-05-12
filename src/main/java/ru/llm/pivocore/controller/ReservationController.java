package ru.llm.pivocore.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.ReservationRequest;
import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.dto.RestaurantUserDto;
import ru.llm.pivocore.service.ReservationService;

import java.util.List;

@RestController
//@RequestMapping(value = "/api/app_user")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

//    @GetMapping(value = "/reserve")
//    public @ResponseBody ReservationDto reserve(@RequestBody ReservationRequest reservationRequest) {
//        return reservationService.createReservation(reservationRequest);
//    }
}

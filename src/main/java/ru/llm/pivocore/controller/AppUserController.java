package ru.llm.pivocore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.dto.ReservationDto;

import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.model.request.AppUserRegisterRequest;
import ru.llm.pivocore.model.request.AppUserRegisterRequest;
import ru.llm.pivocore.model.request.ReservationRequest;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.dto.ReviewDto;
import ru.llm.pivocore.model.request.AppUserRegisterRequest;
import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.model.request.ReservationRequest;
import ru.llm.pivocore.service.AppUserService;
import ru.llm.pivocore.service.ReservationService;
import ru.llm.pivocore.service.ReservationService;
import ru.llm.pivocore.service.ReviewService;
import ru.llm.pivocore.service.ReservationService;
import ru.llm.pivocore.service.ReservationService;
import ru.llm.pivocore.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/app_user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    private final ReviewService reviewService;
    private final ReservationService reservationService;

    @GetMapping(value = "")
    public @ResponseBody List<AppUserDto> getAllAppUsers() {
        return appUserService.getAll();
    }

    @PostMapping(value = "/register")
    public @ResponseBody AppUserDto registerAsNewUser(
            @RequestBody AppUserRegisterRequest registerRequest
    ) {
        return appUserService.registerUser(registerRequest);
    }

    @GetMapping(value = { "{userId}/reviews/", "/reviews" })
    public @ResponseBody List<ReviewDto> gatherReviews(@PathVariable(required = false) Long userId) {
        return reviewService.gatherReviewsForUser(userId);
    }

    @PostMapping(value = "/reserve")
    public @ResponseBody ReservationDto reserve(@RequestBody ReservationRequest reservationRequest) {
        return reservationService.createReservation(reservationRequest);
    }

}

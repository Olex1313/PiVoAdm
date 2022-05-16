package ru.llm.pivocore.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.dto.ReservationResponseDto;
import ru.llm.pivocore.model.request.ReservationRequest;
import ru.llm.pivocore.model.request.RestaurantUserRegisterRequest;
import ru.llm.pivocore.model.dto.RestaurantUserDto;
import ru.llm.pivocore.service.ReservationService;
import ru.llm.pivocore.service.RestaurantService;
import ru.llm.pivocore.service.RestaurantUserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/restaurant_user")
@RequiredArgsConstructor
public class RestaurantUserController {

    private final RestaurantUserService restaurantUserService;


    @GetMapping(value = "/")
    public @ResponseBody List<RestaurantUserDto> getAllRestaurantUsers() {
        return restaurantUserService.getAll();
    }

    @PostMapping(value = "/register")
    public @ResponseBody RestaurantUserDto registerAsNewRestaurantUser(
            @RequestBody RestaurantUserRegisterRequest registerRequest
    ) {
        return restaurantUserService.register(registerRequest);
    }

}

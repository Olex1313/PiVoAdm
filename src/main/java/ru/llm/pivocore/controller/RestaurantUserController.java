package ru.llm.pivocore.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.AppUserRegisterRequest;
import ru.llm.pivocore.model.RestaurantUserRegisterRequest;
import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.model.dto.RestaurantUserDto;
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

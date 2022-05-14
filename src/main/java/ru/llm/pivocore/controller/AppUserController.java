package ru.llm.pivocore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.request.AppUserRegisterRequest;
import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.service.AppUserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/app_user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping(value = "/")
    public @ResponseBody List<AppUserDto> getAllAppUsers() {
        return appUserService.getAll();
    }

    @PostMapping(value = "/register")
    public @ResponseBody AppUserDto registerAsNewUser(
            @RequestBody AppUserRegisterRequest registerRequest
    ) {
        return appUserService.registerUser(registerRequest);
    }

}

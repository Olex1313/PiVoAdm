package ru.llm.pivocore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.dto.CuisineDto;
import ru.llm.pivocore.service.CuisineService;

import java.util.List;

@RestController
@RequestMapping("/api/cuisine")
@RequiredArgsConstructor
public class CuisineController {

    private final CuisineService service;

    @GetMapping("/")
    List<CuisineDto> getAllCuisines() {
        return service.getAllCuisines();
    }

    @PostMapping("/")
    CuisineDto createCuisine(@RequestBody CuisineDto cuisineDto) {
        return service.createCuisine(cuisineDto);
    }

}

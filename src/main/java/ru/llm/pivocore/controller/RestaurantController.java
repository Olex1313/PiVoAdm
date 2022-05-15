package ru.llm.pivocore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.dto.RestaurantDto;
import ru.llm.pivocore.model.dto.ReviewDto;
import ru.llm.pivocore.model.request.CreateRestaurantRequest;
import ru.llm.pivocore.model.request.UpdateRestaurantRequest;
import ru.llm.pivocore.service.RestaurantService;
import ru.llm.pivocore.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    private final ReviewService reviewService;


    @PostMapping(value = "/")
    private @ResponseBody RestaurantDto createRestaurant(
            @RequestBody CreateRestaurantRequest request
    ) {
        return restaurantService.createRestaurant(request);
    }

    @GetMapping(value = "/")
    private @ResponseBody List<RestaurantDto> listRestaurants() {
        return restaurantService.listRestaurants();
    }

    @PatchMapping(value = "/")
    private @ResponseBody RestaurantDto updateRestaurant(
            @RequestBody UpdateRestaurantRequest request
    ) {
        return restaurantService.updateRestaurant(request);
    }

    @GetMapping(value = { "/reviews/{restaurantId}", "/reviews" })
    private @ResponseBody List<ReviewDto> gatherReviews(
            @PathVariable(required = false) Long restaurantId
    ) {
        return reviewService.gatherReviewsForRestaurant(restaurantId);
    }

}

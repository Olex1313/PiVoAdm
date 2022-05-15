package ru.llm.pivocore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.dto.ReviewDto;
import ru.llm.pivocore.model.request.CreateReviewRequest;
import ru.llm.pivocore.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "")
    public @ResponseBody ReviewDto leaveReview(@RequestBody CreateReviewRequest request) {
        return reviewService.leaveReview(request);
    }

}

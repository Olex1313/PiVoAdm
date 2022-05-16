package ru.llm.pivocore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.llm.pivocore.configuration.security.suppliers.UserContextSupplier;
import ru.llm.pivocore.exception.ReviewException;
import ru.llm.pivocore.mapper.ReviewMapper;
import ru.llm.pivocore.model.dto.ReviewDto;
import ru.llm.pivocore.model.entity.RestaurantEntity;
import ru.llm.pivocore.model.entity.ReviewEntity;
import ru.llm.pivocore.model.request.CreateReviewRequest;
import ru.llm.pivocore.repository.ReviewRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;

    private final UserContextSupplier userContextSupplier;

    private final RestaurantService restaurantService;

    private final ReviewMapper mapper;

    @Transactional
    public List<ReviewDto> gatherReviewsForUser(Long userId) {
        if (userId == null) {
            log.info("Missing userId in path, gathering for current user");
            val currentUser = userContextSupplier.getAppUserEntityFromSecContext();
            userId = currentUser.getId();
        }
        return retrieveReviewsForAppUserId(userId);
    }

    @Transactional
    public List<ReviewDto> gatherReviewsForRestaurant(Long restaurantId) {
        if (restaurantId == null) {
            log.info("Missing userId in path, gathering for current user restaurants");
            val currentUser = userContextSupplier.getRestaurantUserEntityFromSecContext();
            val restaurantsIdForCurrentUser = currentUser.getRestaurantList().stream()
                    .map(RestaurantEntity::getId)
                    .collect(Collectors.toSet());
            val reviews = repository.findAll().stream()
                    .filter(review -> restaurantsIdForCurrentUser.contains(review.getRestaurant().getId()))
                    .map(mapper::entityToDto)
                    .collect(Collectors.toList());
            return reviews;
        }
        return retrieveReviewsForRestaurant(restaurantId);
    }

    @Transactional
    public ReviewDto leaveReview(CreateReviewRequest request) {
        val entity = new ReviewEntity();
        try {
            entity.setComment(request.getComment());
            entity.setScore(request.getScore());
            entity.setAppUser(userContextSupplier.getAppUserEntityFromSecContext());
            entity.setRestaurant(restaurantService.getById(request.getRestaurantId()));
            return mapper.entityToDto(repository.save(entity));
        } catch (Exception e) {
            throw new ReviewException(e.getMessage(), e.getCause());
        }
    }

    private List<ReviewDto> retrieveReviewsForAppUserId(Long userId) {
        return repository.findAll().stream()
                .filter(review -> review.getAppUser().getId().equals(userId))
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    private List<ReviewDto> retrieveReviewsForRestaurant(Long restaurantId) {
        return repository.findAll().stream()
                .filter(review -> review.getRestaurant().getId().equals(restaurantId))
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

}

package ru.llm.pivocore.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.llm.pivocore.exception.RestaurantCreateException;
import ru.llm.pivocore.exception.RestaurantException;
import ru.llm.pivocore.mapper.RestaurantMapper;
import ru.llm.pivocore.model.dto.RestaurantDto;
import ru.llm.pivocore.model.entity.RestaurantEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;
import ru.llm.pivocore.model.request.CreateRestaurantRequest;
import ru.llm.pivocore.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository repository;

    private final RestaurantUserService userService;

    private final RestaurantMapper mapper;

    @Transactional
    public RestaurantDto createRestaurant(CreateRestaurantRequest request) {
        try {
            val restaurantUser = userService.getCurrentUserFromSecContext();
            val rawDto = RestaurantDto.builder()
                    .email(request.getEmail())
                    .location(request.getLocation())
                    .name(request.getName())
                    .phoneNumber(request.getPhoneNumber())
                    .website(request.getWebsite())
                    .isActive(true)
                    .build();
            val restaurantEntity = mapper.dtoToEntity(rawDto);
            linkRestaurantUserToRestaurant(restaurantUser, restaurantEntity);
            repository.save(restaurantEntity);
            return rawDto;
        } catch (Exception e) {
            throw new RestaurantCreateException(e.getMessage(), e.getCause());
        }
    }

    @Transactional
    public List<RestaurantDto> listRestaurants() {
        try {
            val restaurantUser = userService.getCurrentUserFromSecContext();
            val restaurants = restaurantUser.getRestaurantList();
            return restaurants.stream().map(mapper::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RestaurantException(e.getMessage(), e.getCause());
        }
    }

    private void linkRestaurantUserToRestaurant(RestaurantUserEntity user, RestaurantEntity restaurant) {
        if (restaurant.getRestaurantUsers() == null) {
            restaurant.setRestaurantUsers(new ArrayList<>());
            restaurant.getRestaurantUsers().add(user);
        } else {
            restaurant.getRestaurantUsers().add(user);
        }
    }

}

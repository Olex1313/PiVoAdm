package ru.llm.pivocore.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.llm.pivocore.configuration.security.suppliers.UserContextSupplier;
import ru.llm.pivocore.exception.RestaurantCreateException;
import ru.llm.pivocore.exception.RestaurantException;
import ru.llm.pivocore.exception.RestaurantNotFoundException;
import ru.llm.pivocore.exception.RestaurantUpdateException;
import ru.llm.pivocore.mapper.RestaurantMapper;
import ru.llm.pivocore.model.dto.RestaurantDto;
import ru.llm.pivocore.model.entity.CuisineEntity;
import ru.llm.pivocore.model.entity.RestaurantEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;
import ru.llm.pivocore.model.request.CreateRestaurantRequest;
import ru.llm.pivocore.model.request.UpdateRestaurantRequest;
import ru.llm.pivocore.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository repository;
    private final UserContextSupplier userContextSupplier;
    private final RestaurantMapper restaurantMapper;
    private final CuisineService cuisineService;

    @Transactional
    public RestaurantDto createRestaurant(CreateRestaurantRequest request) {
        try {
            val restaurantUser = userContextSupplier.getRestaurantUserEntityFromSecContext();
            val rawDto = RestaurantDto.builder()
                    .email(request.getEmail())
                    .location(request.getLocation())
                    .name(request.getName())
                    .phoneNumber(request.getPhoneNumber())
                    .website(request.getWebsite())
                    .isActive(true)
                    .build();
            val restaurantEntity = restaurantMapper.dtoToEntity(rawDto);
            val cuisines = request.getCuisines().stream()
                    .map(cuisineService::getById)
                    .toList();
            linkRestaurantUserToRestaurant(restaurantUser, restaurantEntity);
            for (val cuisine : cuisines) {
                linkCuisinesToRestaurant(cuisine, restaurantEntity);
            }
            return restaurantMapper.entityToDto(repository.save(restaurantEntity));
        } catch (Exception e) {
            throw new RestaurantCreateException(e.getMessage(), e.getCause());
        }
    }

    @Transactional
    public List<RestaurantDto> listRestaurants() {
        try {
            val restaurantUser = userContextSupplier.getRestaurantUserEntityFromSecContext();
            val restaurants = restaurantUser.getRestaurantList();
            return restaurants.stream().map(restaurantMapper::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RestaurantException(e.getMessage(), e.getCause());
        }
    }

    @Transactional
    public RestaurantDto updateRestaurant(UpdateRestaurantRequest request) {
        val user = userContextSupplier.getRestaurantUserEntityFromSecContext();
        val restaurants = user.getRestaurantList();
        val restaurantToUpdate = restaurants.stream()
                .filter(restaurant -> restaurant.getId().equals(request.getRestaurantId())).findFirst();
        if (restaurantToUpdate.isEmpty()) {
            throw new RestaurantUpdateException("No such id");
        }
        var updated = restaurantToUpdate.get();
        updated.setEmail(request.getEmail() != null ? request.getEmail() : updated.getEmail());
        updated.setLocation(request.getLocation() != null ? request.getLocation() : updated.getLocation());
        updated.setName(request.getName() != null ? request.getName() : updated.getName());
        updated.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : updated.getPhoneNumber());
        updated.setIsActive(request.getIsActive() != null ? request.getIsActive() : updated.getIsActive());
        updated = repository.save(updated);
        restaurantToUpdate.get().setEmail(request.getEmail());
        return restaurantMapper.entityToDto(updated);
    }

    private void linkRestaurantUserToRestaurant(RestaurantUserEntity user, RestaurantEntity restaurant) {
        if (restaurant.getRestaurantUsers() == null) {
            restaurant.setRestaurantUsers(new ArrayList<>());
            restaurant.getRestaurantUsers().add(user);
        } else {
            restaurant.getRestaurantUsers().add(user);
        }
    }

    private void linkCuisinesToRestaurant(CuisineEntity cuisine, RestaurantEntity restaurant) {
        if(restaurant.getCuisines() == null) {
            restaurant.setCuisines(new ArrayList<>());
        }
        val cuisines = restaurant.getCuisines();
        cuisines.add(cuisine);
    }

    public RestaurantEntity getById(Long restaurantId) {
        try {
            return repository.getById(restaurantId);
        } catch (Exception e) {
            throw new RestaurantNotFoundException(e.getMessage(), e.getCause());
        }
    }

}

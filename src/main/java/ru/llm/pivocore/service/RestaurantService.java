package ru.llm.pivocore.service;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.llm.pivocore.exception.RestaurantCreateException;
import ru.llm.pivocore.exception.RestaurantException;
import ru.llm.pivocore.exception.RestaurantNotFoundException;
import ru.llm.pivocore.exception.RestaurantUserServiceException;
import ru.llm.pivocore.mapper.ReservationMapper;
import ru.llm.pivocore.mapper.RestaurantMapper;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.dto.ReservationResponseDto;
import ru.llm.pivocore.model.dto.RestaurantDto;
import ru.llm.pivocore.model.entity.ReservationEntity;
import ru.llm.pivocore.exception.RestaurantCreateException;
import ru.llm.pivocore.exception.RestaurantException;
import ru.llm.pivocore.exception.RestaurantNotFoundException;
import ru.llm.pivocore.exception.RestaurantUpdateException;
import ru.llm.pivocore.mapper.RestaurantMapper;
import ru.llm.pivocore.model.dto.RestaurantDto;
import ru.llm.pivocore.model.entity.RestaurantEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;
import ru.llm.pivocore.model.request.CreateRestaurantRequest;
import ru.llm.pivocore.model.request.UpdateRestaurantRequest;
import ru.llm.pivocore.repository.ReservationsRepository;
import ru.llm.pivocore.repository.RestaurantRepository;
import ru.llm.pivocore.repository.RestaurantUsersRepository;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantUsersRepository restaurantUsersRepository;
    private final ReservationsRepository reservationsRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationMapper reservationMapper;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantUserService userService;


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
            val restaurantEntity = restaurantMapper.dtoToEntity(rawDto);
            linkRestaurantUserToRestaurant(restaurantUser, restaurantEntity);
            restaurantRepository.save(restaurantEntity);
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
            return restaurants.stream().map(restaurantMapper::entityToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RestaurantException(e.getMessage(), e.getCause());
        }
    }


    @Transactional
    public RestaurantDto updateRestaurant(UpdateRestaurantRequest request) {
        val user = userService.getCurrentUserFromSecContext();
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
        updated = restaurantRepository.save(updated);
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

    public RestaurantEntity getById(Long restaurantId) {
        try {
            return restaurantRepository.getById(restaurantId);
        } catch (Exception e) {
            throw new RestaurantNotFoundException(e.getMessage(), e.getCause());
        }
    }
    @Transactional
    public List<ReservationDto> getAllReservations(Long restaurantId) {
        val restaurantUserEntity = getCurrentUserFromSecContext();
        Optional<RestaurantEntity> restaurant = restaurantUserEntity.getRestaurantList()
                .stream().filter(rest -> rest.getId().equals(restaurantId))
                .findFirst();
        if (restaurant.isEmpty()) {
            throw new RestaurantException("Restaurant is not found!");
        }
        return restaurant.get().getReservations()
                .stream().map(reservationMapper::entityToDto).toList();
    }

    public ReservationResponseDto approveById(Long reservationId) {
        RestaurantUserEntity restaurantUserEntity = getCurrentUserFromSecContext();
        ReservationEntity reservationEntity = reservationsRepository.getById(reservationId);
        // check tables
        reservationEntity.setRestaurantUser(restaurantUserEntity);
        reservationEntity.setApprove_time(Instant.now());
            return new ReservationResponseDto(
                reservationId,
                true
        );
    }

    @Transactional
    public RestaurantUserEntity getCurrentUserFromSecContext() {
        val currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            return restaurantUsersRepository.findByUsername(currentUsername);
        } catch (Exception e) {
            throw new RestaurantUserServiceException(
                    "Couldn't retrieve restaurant user for current user:%s in session".formatted(currentUsername)
            );
        }
    }

    @Transactional
    public RestaurantDto updateRestaurant(UpdateRestaurantRequest request) {
        val user = userService.getCurrentUserFromSecContext();
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
        return mapper.entityToDto(updated);
    }

    private void linkRestaurantUserToRestaurant(RestaurantUserEntity user, RestaurantEntity restaurant) {
        if (restaurant.getRestaurantUsers() == null) {
            restaurant.setRestaurantUsers(new ArrayList<>());
            restaurant.getRestaurantUsers().add(user);
        } else {
            restaurant.getRestaurantUsers().add(user);
        }
    }

    public RestaurantEntity getById(Long restaurantId) {
        try {
            return repository.getById(restaurantId);
        } catch (Exception e) {
            throw new RestaurantNotFoundException(e.getMessage(), e.getCause());
        }
    }

}

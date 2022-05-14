package ru.llm.pivocore.service;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.llm.pivocore.exception.RestaurantUserServiceException;
import ru.llm.pivocore.mapper.ReservationMapper;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.dto.ReservationResponseDto;
import ru.llm.pivocore.model.entity.ReservationEntity;
import ru.llm.pivocore.exception.RestaurantCreateException;
import ru.llm.pivocore.exception.RestaurantException;
import ru.llm.pivocore.exception.RestaurantNotFoundException;
import ru.llm.pivocore.exception.RestaurantUpdateException;
import ru.llm.pivocore.mapper.RestaurantMapper;
import ru.llm.pivocore.model.dto.RestaurantDto;
import ru.llm.pivocore.model.entity.RestaurantEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;
import ru.llm.pivocore.repository.ReservationsRepository;
import ru.llm.pivocore.repository.RestaurantUsersRepository;


import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {

    private final RestaurantUsersRepository restaurantUsersRepository;
    private final ReservationsRepository reservationsRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public List<ReservationDto> getAllReservations(Integer restaurantId) {
        RestaurantUserEntity restaurantUserEntity = getCurrentUserFromSecContext();
        RestaurantEntity restaurant = restaurantUserEntity.getRestaurantList().get(restaurantId);
        return restaurant.getReservationEntities().stream().map(reservationMapper::entityToDto).toList();
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

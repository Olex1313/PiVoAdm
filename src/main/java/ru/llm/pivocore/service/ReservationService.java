package ru.llm.pivocore.service;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.llm.pivocore.configuration.security.facade.IAuthenticationFacade;
import ru.llm.pivocore.exception.RestaurantException;
import ru.llm.pivocore.mapper.ReservationMapper;
import ru.llm.pivocore.model.dto.ReservationResponseDto;
import ru.llm.pivocore.model.entity.RestaurantEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;
import ru.llm.pivocore.model.request.ReservationRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.llm.pivocore.configuration.security.facade.IAuthenticationFacade;
import ru.llm.pivocore.mapper.AppUserMapper;
import ru.llm.pivocore.mapper.ReservationMapper;
import ru.llm.pivocore.model.ReservationRequest;
import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.entity.AppUserEntity;
import ru.llm.pivocore.model.entity.ReservationEntity;
import ru.llm.pivocore.repository.AppUserRepository;
import ru.llm.pivocore.repository.ReservationsRepository;
import ru.llm.pivocore.repository.RestaurantRepository;
import ru.llm.pivocore.repository.RestaurantUsersRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ReservationService {

    private final IAuthenticationFacade authenticationFacade;
    private final ReservationMapper reservationMapper;
    private final ReservationsRepository reservationsRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantUsersRepository restaurantUsersRepository;
    private final AppUserRepository appUserRepository;


    @Transactional
    public List<ReservationDto> getAllReservations(Long restaurantId) {
        List<ReservationDto> dtoToReturn = new ArrayList<>();
        val restaurantUserEntity = getCurrentRestUserFromSecContext();
        Optional<RestaurantEntity> restaurant = restaurantUserEntity.getRestaurantList()
                .stream().filter(rest -> rest.getId().equals(restaurantId))
                .findFirst();
        if (restaurant.isEmpty()) {
            throw new RestaurantException("Restaurant is not found!");
        }
        List<ReservationEntity> currReservationsAssignedToRestaurant = restaurant.get().getReservations();
        if (currReservationsAssignedToRestaurant != null) {
            currReservationsAssignedToRestaurant.
                forEach(
                        reservationEntity -> {reservationEntity.setRestaurantUser(restaurantUserEntity);
                        });
            List<ReservationEntity> currReservationsUserHas = restaurantUserEntity.getReservations();
            if (currReservationsUserHas == null) {
                currReservationsUserHas = new ArrayList<>();
            }
            currReservationsAssignedToRestaurant.stream()
                    .filter(reservationEntity -> reservationEntity.getRestaurantUser() == null)
                    .forEach(currReservationsUserHas::add);
            dtoToReturn = reservationsRepository.
                    saveAll(currReservationsAssignedToRestaurant)
                    .stream()
                    .map(reservationMapper::entityToDto)
                    .collect(Collectors.toList());
        }
        return dtoToReturn;
    }


    public ReservationResponseDto approveById(Long reservationId) {
        RestaurantUserEntity restaurantUserEntity = getCurrentRestUserFromSecContext();
        ReservationEntity reservationEntity = reservationsRepository.getById(reservationId);
        // check tables
        reservationEntity.setRestaurantUser(restaurantUserEntity);
        reservationEntity.setApprove_time(Instant.now());
        return new ReservationResponseDto(
                reservationId,
                true
        );
    }


    public ReservationDto createReservation(ReservationRequest reservationRequest) {
        ReservationDto reservationDto = ReservationDto.builder()
                .restaurantId(reservationRequest.getRestaurantId())
                .deposit(reservationRequest.getDeposit())
                .amountOfGuests(reservationRequest.getAmountOfGuests())
                .startReservationTime(reservationRequest.getStartReservationTime())
                .endReservationTime(reservationRequest.getEndReservationTime())
                .build();
        val appUserEntity = getCurrentAppUserFromSecContext();
        val reservationEntity = reservationMapper.dtoToEntity(reservationDto);
        reservationEntity.setUser(appUserEntity);
        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(reservationRequest.getRestaurantId());
        if (restaurant.isEmpty()) {
            throw new RestaurantException("Restaurant is not found!");
        }
        RestaurantEntity restaurantEntity = restaurant.get();
        reservationEntity.setRestaurant(restaurantEntity);
        linkReservationToRestaurant(reservationEntity, restaurantEntity);
        return reservationMapper.entityToDto(reservationsRepository.save(reservationEntity));
    }

    private void linkReservationToRestaurant(ReservationEntity reservation, RestaurantEntity restaurant) {
        if (restaurant.getReservations() == null) {
            restaurant.setReservations(new ArrayList<>());
        }
        restaurant.getReservations().add(reservation);
    }

    private AppUserEntity getCurrentAppUserFromSecContext() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return appUserRepository.findByUsername(authentication.getName());
   }

    private RestaurantUserEntity getCurrentRestUserFromSecContext() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return restaurantUsersRepository.findByUsername(authentication.getName());
    }
}

package ru.llm.pivocore.service;

import lombok.AllArgsConstructor;
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

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ReservationService {

    private final IAuthenticationFacade authenticationFacade;
    private final ReservationMapper reservationMapper;
    private final ReservationsRepository reservationsRepository;
    private final RestaurantRepository restaurantRepository;
    private final AppUserRepository appUserRepository;


    public ReservationDto createReservation(ReservationRequest reservationRequest) {
        ReservationDto reservationDto = ReservationDto.builder()
                .restaurantId(reservationRequest.getRestaurantId())
                .deposit(reservationRequest.getDeposit())
                .amountOfGuests(reservationRequest.getAmountOfGuests())
                .startReservationTime(reservationRequest.getStartReservationTime())
                .endReservationTime(reservationRequest.getEndReservationTime())
                .build();
        Authentication authentication = authenticationFacade.getAuthentication();
        AppUserEntity appUserEntity = appUserRepository.findByUsername(authentication.getName());
        ReservationEntity reservationEntity = reservationMapper.dtoToEntity(reservationDto);
        reservationEntity.setUser(appUserEntity);
        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(reservationRequest.getRestaurantId());
        if (restaurant.isEmpty()) {
            throw new RestaurantException("Restaurant is not found!");
        }
        reservationEntity.setRestaurant(restaurant.get());
        reservationsRepository.save(reservationEntity);
        return reservationDto;
    }


}

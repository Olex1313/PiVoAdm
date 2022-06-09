package ru.llm.pivocore.service;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.llm.pivocore.configuration.security.suppliers.UserContextSupplier;
import ru.llm.pivocore.exception.*;
import ru.llm.pivocore.mapper.ReservationMapper;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.dto.ReservationResponseDto;
import ru.llm.pivocore.model.entity.ReservationEntity;
import ru.llm.pivocore.model.entity.RestaurantEntity;
import ru.llm.pivocore.model.entity.RestaurantTableEntity;
import ru.llm.pivocore.model.request.ReservationRequest;
import ru.llm.pivocore.repository.ReservationsRepository;
import ru.llm.pivocore.repository.RestaurantRepository;
import ru.llm.pivocore.repository.TableRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {

    private final UserContextSupplier userContextSupplier;
    private final ReservationMapper reservationMapper;
    private final ReservationsRepository reservationsRepository;
    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;


    @Transactional
    public List<ReservationDto> getAllReservations(Long restaurantId) {
        List<ReservationDto> dtoToReturn = new ArrayList<>();
        val restaurantUserEntity = userContextSupplier.getRestaurantUserEntityFromSecContext();
        Optional<RestaurantEntity> restaurant = restaurantUserEntity.getRestaurantList()
                .stream().filter(rest -> rest.getId().equals(restaurantId))
                .findFirst();
        if (restaurant.isEmpty()) {
            throw new RestaurantException("Restaurant is not found!");
        }
        List<ReservationEntity> currReservationsAssignedToRestaurant = restaurant.get().getReservations();
        if (currReservationsAssignedToRestaurant != null) {
            currReservationsAssignedToRestaurant.
                    forEach(reservationEntity -> reservationEntity.setRestaurantUser(restaurantUserEntity));
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

    public ReservationResponseDto approveReservationAssignedToRestaurant(Long restaurantId, Long reservationId) {
        val restaurantEntity = restaurantRepository.getById(restaurantId);
        val reservationEntity = reservationsRepository.getById(reservationId);
        Optional<RestaurantTableEntity> suitableTable;
        try {
            suitableTable = findSuitableTable(restaurantEntity, reservationEntity);
        } catch (RuntimeException e) {
            throw new ApproveReservationException(e.getMessage());
        }
        if (suitableTable.isEmpty()) {
            throw new DeclineReservationException("There are no suitable tables!");
        }
        RestaurantTableEntity restaurantTable = suitableTable.get();
        restaurantTable.setIsActive(true);
        tableRepository.save(restaurantTable);
        reservationEntity.setApprove_time(Instant.now());
        reservationEntity.setRestaurantTable(restaurantTable);
        reservationsRepository.save(reservationEntity);
        return ReservationResponseDto.
                builder()
                .reservationId(reservationId)
                .isApproved(true)
                .build();
    }

    public ReservationDto createReservation(ReservationRequest reservationRequest) {
        ReservationDto reservationDto = ReservationDto.builder()
                .restaurantId(reservationRequest.getRestaurantId())
                .deposit(reservationRequest.getDeposit())
                .amountOfGuests(reservationRequest.getAmountOfGuests())
                .startReservationTime(reservationRequest.getStartReservationTime())
                .endReservationTime(reservationRequest.getEndReservationTime())
                .build();
        val appUserEntity = userContextSupplier.getAppUserEntityFromSecContext();
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

    private Optional<RestaurantTableEntity> findSuitableTable(RestaurantEntity restaurant, ReservationEntity reservation) {
        if (restaurant == null) {
            throw new RestaurantNotFoundException("Restaurant does not exist;");
        }
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation does not exist");
        }
        List<RestaurantTableEntity> finalRestaurantTables = restaurant.getRestaurantTables();
        if (finalRestaurantTables == null) {
            throw new TableNotFoundException("Restaurant does not have any tables");
        }
        return finalRestaurantTables.stream()
                .filter(table -> reservation.getAmountOfGuests() <= table.getMaxAmount() && !table.getIsActive())
                .findFirst();
    }

}

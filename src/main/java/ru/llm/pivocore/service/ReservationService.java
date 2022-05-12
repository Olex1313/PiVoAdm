package ru.llm.pivocore.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
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

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ReservationService {

    private final IAuthenticationFacade authenticationFacade;

    private final ReservationMapper reservationMapper;

    private final ReservationsRepository reservationsRepository;

    private final AppUserRepository appUserRepository;

    public ReservationDto createReservation(ReservationRequest reservationRequest) {
        ReservationDto reservationDto = ReservationDto.builder()
                .deposit(reservationRequest.getDeposit())
                .amountOfGuests(reservationRequest.getAmountOfGuests())
                .time(LocalDate.now())
                .build();
        Authentication authentication = authenticationFacade.getAuthentication();
        AppUserEntity appUserEntity = appUserRepository.findByUsername(authentication.getName());
        ReservationEntity reservationEntity = reservationMapper.dtoToEntity(reservationDto);
        reservationEntity.setUser(appUserEntity);
        return reservationMapper.entityToDto(reservationsRepository.save(reservationEntity));
    }

}

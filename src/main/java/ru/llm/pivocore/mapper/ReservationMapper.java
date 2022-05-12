package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.entity.ReservationEntity;

@Mapper
public interface ReservationMapper {
    ReservationDto entityToDto(ReservationEntity reservationEntity);
    ReservationEntity dtoToEntity(ReservationDto reservationDto);
}

package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.llm.pivocore.exception.RestaurantNotFoundException;
import ru.llm.pivocore.model.dto.ReservationDto;
import ru.llm.pivocore.model.dto.RestaurantDto;
import ru.llm.pivocore.model.entity.ReservationEntity;
import ru.llm.pivocore.model.entity.RestaurantEntity;

@Mapper
public interface ReservationMapper {
    @Mapping(target = "restaurantId", source = "reservationEntity.restaurant.id")
    ReservationDto entityToDto(ReservationEntity reservationEntity);
    ReservationEntity dtoToEntity(ReservationDto reservationDto);

}

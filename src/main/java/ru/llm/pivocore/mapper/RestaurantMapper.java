package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.llm.pivocore.model.dto.RestaurantDto;
import ru.llm.pivocore.model.entity.RestaurantEntity;

@Mapper
public interface RestaurantMapper {

    @Mapping(target = "restaurantId", source = "entity.id")
    RestaurantDto entityToDto(RestaurantEntity entity);

    @Mapping(source = "restaurantId", target = "id")
    RestaurantEntity dtoToEntity(RestaurantDto dto);

}

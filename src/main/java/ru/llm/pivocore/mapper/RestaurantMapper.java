package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.llm.pivocore.model.dto.CuisineDto;
import ru.llm.pivocore.model.dto.RestaurantDto;
import ru.llm.pivocore.model.entity.CuisineEntity;
import ru.llm.pivocore.model.entity.RestaurantEntity;

import javax.persistence.ManyToOne;

@Mapper
public interface RestaurantMapper {

    @Mapping(target = "restaurantId", source = "entity.id")
    RestaurantDto entityToDto(RestaurantEntity entity);

    @Mapping(source = "restaurantId", target = "id")
    RestaurantEntity dtoToEntity(RestaurantDto dto);

}

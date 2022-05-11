package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.model.dto.RestaurantUserDto;
import ru.llm.pivocore.model.entity.AppUserEntity;
import ru.llm.pivocore.model.entity.RestaurantUserEntity;

@Mapper
public interface RestaurantUserMapper {

    @Mapping(target = "enabled", source="userName", defaultValue = "true")
    RestaurantUserDto entityToDto(RestaurantUserEntity entity);

    RestaurantUserEntity dtoToEntity(RestaurantUserDto dto);

}

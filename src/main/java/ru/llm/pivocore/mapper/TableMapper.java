package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import ru.llm.pivocore.model.dto.TableDto;
import ru.llm.pivocore.model.entity.RestaurantTableEntity;

@Mapper
public interface TableMapper {

    TableDto entityToDto(RestaurantTableEntity tableEntity);
    RestaurantTableEntity dtoToEntity(TableDto tableDto);
}

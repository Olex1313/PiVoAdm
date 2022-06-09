package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import ru.llm.pivocore.model.dto.CuisineDto;
import ru.llm.pivocore.model.entity.CuisineEntity;

@Mapper
public interface CuisineMapper {

    CuisineDto entityToDto(CuisineEntity entity);

    CuisineEntity dtoToEntity(CuisineDto dto);

}

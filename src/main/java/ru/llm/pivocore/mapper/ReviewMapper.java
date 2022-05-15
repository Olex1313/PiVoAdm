package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import ru.llm.pivocore.model.dto.ReviewDto;
import ru.llm.pivocore.model.entity.ReviewEntity;

@Mapper
public interface ReviewMapper {

    ReviewDto entityToDto(ReviewEntity entity);

    ReviewEntity dtoToEntity(ReviewDto dto);

}

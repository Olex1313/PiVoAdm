package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.model.entity.AppUserEntity;

@Mapper
public interface AppUserMapper {

    AppUserDto entityToDto(AppUserEntity entity);

    AppUserEntity dtoToEntity(AppUserDto dto);

}

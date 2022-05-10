package ru.llm.pivocore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.llm.pivocore.model.dto.AppUserDto;
import ru.llm.pivocore.model.entity.AppUserEntity;

@Mapper
public interface AppUserMapper {

    @Mapping(target = "enabled", source="username", defaultValue = "true")
    AppUserDto entityToDto(AppUserEntity entity);

    AppUserEntity dtoToEntity(AppUserDto dto);

}

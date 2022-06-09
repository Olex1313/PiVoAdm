package ru.llm.pivocore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.llm.pivocore.exception.CuisineException;
import ru.llm.pivocore.mapper.CuisineMapper;
import ru.llm.pivocore.model.dto.CuisineDto;
import ru.llm.pivocore.model.entity.CuisineEntity;
import ru.llm.pivocore.repository.CuisineRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuisineService {

    private final CuisineRepository repository;
    private final CuisineMapper mapper;

    public List<CuisineDto> getAllCuisines() {
        try {
            return repository.findAll().stream()
                    .map(mapper::entityToDto)
                    .toList();
        } catch (Exception e) {
            throw new CuisineException(e);
        }
    }

    public CuisineDto createCuisine(CuisineDto cuisineDto) {
        var entity = mapper.dtoToEntity(cuisineDto);
        try {
            return mapper.entityToDto(repository.save(entity));
        } catch (Exception e) {
            throw new CuisineException(e);
        }
    }

    public CuisineEntity getById(Long id) {
        try {
            return repository.getById(id);
        } catch (Exception e) {
            throw new CuisineException(e);
        }
    }

}

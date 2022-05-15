package ru.llm.pivocore.service;

import lombok.val;
import org.springframework.stereotype.Service;
import ru.llm.pivocore.mapper.TableMapper;
import ru.llm.pivocore.model.dto.TableDto;
import ru.llm.pivocore.model.entity.RestaurantTableEntity;
import ru.llm.pivocore.model.request.CreateTableRequest;
import ru.llm.pivocore.repository.TableRepository;

@Service
public class TableService {

    private TableRepository tableRepository;
    private TableMapper tableMapper;

    public TableDto createTable(CreateTableRequest request) {
        val tableDto = TableDto.builder()
                .tableNum(request.getTableNum())
                .maxAmount(request.getMaxAmount())
                .build();
        RestaurantTableEntity tableEntity = tableMapper.dtoToEntity(tableDto);

        return tableMapper.entityToDto(tableRepository.save(tableEntity));
    }
}

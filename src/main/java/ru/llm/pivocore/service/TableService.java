package ru.llm.pivocore.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Service;
import ru.llm.pivocore.configuration.security.suppliers.UserContextSupplier;
import ru.llm.pivocore.exception.BadRequestException;
import ru.llm.pivocore.exception.RestaurantNotFoundException;
import ru.llm.pivocore.exception.TableNotFoundException;
import ru.llm.pivocore.mapper.TableMapper;
import ru.llm.pivocore.model.dto.TableDto;
import ru.llm.pivocore.model.entity.RestaurantEntity;
import ru.llm.pivocore.model.entity.RestaurantTableEntity;
import ru.llm.pivocore.model.request.CreateTableRequest;
import ru.llm.pivocore.repository.RestaurantRepository;
import ru.llm.pivocore.repository.TableRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TableService {

    private TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;

    private TableMapper tableMapper;

    public TableDto createTable(Long restaurantId, CreateTableRequest request) {
        if (request.getTableNum() <= 0 || request.getMaxAmount() <= 0) {
            throw new BadRequestException("invalid request!");
        }
        val tableDto = TableDto.builder()
                .tableNum(request.getTableNum())
                .maxAmount(request.getMaxAmount())
                .isActive(false)
                .build();
        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant.isEmpty()) {
            throw new RestaurantNotFoundException("Couldn't create tables, restaurant does not exist!");
        }
        RestaurantTableEntity tableEntity = tableMapper.dtoToEntity(tableDto);
        tableEntity.setRestaurant(restaurant.get());
        linkTableToRestaurant(restaurant.get(), tableEntity);
        log.info("saving table");
        return tableMapper.entityToDto(tableRepository.save(tableEntity));
    }

    public List<TableDto> getAllTablesOfRestaurant(Long restaurantId) {
        Optional<RestaurantEntity> restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant.isEmpty()) {
            throw new RestaurantNotFoundException("Couldn't find any tables, restaurant does not exist!");
        }
        log.info("restaurant exists");
        List<RestaurantTableEntity> restaurantTables = restaurant.get().getRestaurantTables();;
        if (restaurantTables == null) {
            throw new TableNotFoundException("Restaurant does not have any tables");
        }
        return restaurantTables.stream().map(tableMapper::entityToDto).collect(Collectors.toList());
    }

    private void linkTableToRestaurant(RestaurantEntity restaurant, RestaurantTableEntity tableEntity) {
        List<RestaurantTableEntity> tables = restaurant.getRestaurantTables();
        if (tables == null) {
            tables = new ArrayList<>();
        }
        tables.add(tableEntity);
    }
}

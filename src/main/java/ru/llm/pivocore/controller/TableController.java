package ru.llm.pivocore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.llm.pivocore.model.dto.TableDto;
import ru.llm.pivocore.model.request.CreateTableRequest;
import ru.llm.pivocore.service.TableService;

@RestController
@RequestMapping("/api/restaurant/tables")
@RequiredArgsConstructor
public class TableController {
    private final TableService tableService;

    @PostMapping(value = "/")
    public @ResponseBody TableDto createTable(@RequestParam Long restaurantId,@RequestBody CreateTableRequest request) {
        return tableService.createTable(restaurantId, request);
    }
}

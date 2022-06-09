package ru.llm.pivocore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TableDto {
    @JsonProperty("table_id")
    private Long tableId;
    @JsonProperty("table_num")
    private Short tableNum;
    @JsonProperty("max_amount")
    private Short maxAmount;
    @JsonProperty("is_active")
    private Boolean isActive;
}

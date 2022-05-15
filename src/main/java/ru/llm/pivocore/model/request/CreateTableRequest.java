package ru.llm.pivocore.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTableRequest {
    @JsonProperty("table_num")
    private Short tableNum;
    @JsonProperty("max_amount")
    private Short maxAmount;
}

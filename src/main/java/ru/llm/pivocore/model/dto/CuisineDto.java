package ru.llm.pivocore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CuisineDto {

    @JsonProperty("cuisine_id")
    private Long id;

    @JsonProperty("name")
    private String name;

}

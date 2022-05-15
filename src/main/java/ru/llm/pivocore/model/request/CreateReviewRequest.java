package ru.llm.pivocore.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateReviewRequest {

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("score")
    private BigDecimal score;

    @JsonProperty("restaurant_id")
    private Long restaurantId;

}

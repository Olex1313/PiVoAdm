package ru.llm.pivocore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ReviewDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("score")
    private BigDecimal score;
    @JsonProperty("app_user")
    private AppUserDto appUser;
    @JsonProperty("restaurant")
    private RestaurantDto restaurant;
}

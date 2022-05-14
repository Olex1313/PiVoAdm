package ru.llm.pivocore.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;


@Getter
@Setter
@Builder
public class RestaurantDto {
    private Long restaurantId;
    private String name;
    private String location;
    private String website;
    private String phoneNumber;
    private String email;
    private boolean isActive;
}

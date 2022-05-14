package ru.llm.pivocore.model.entity;

public class RestaurantTableEntity {
    private Long id;
    private Integer tableNum;
    private Integer maxAmount;
    private Boolean isActive;
    private RestaurantEntity restaurant;
}

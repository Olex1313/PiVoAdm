package ru.llm.pivocore.service;

import ru.llm.pivocore.exception.RestaurantException;

public class RestaurantUpdateException extends RestaurantException {

    public RestaurantUpdateException(String message) {
        super(message);
    }

    public RestaurantUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

}

package ru.llm.pivocore.exception;

import ru.llm.pivocore.exception.RestaurantException;

public class RestaurantUpdateException extends RestaurantException {

    public RestaurantUpdateException(String message) {
        super(message);
    }

    public RestaurantUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

}

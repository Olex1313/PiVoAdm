package ru.llm.pivocore.exception;

public class RestaurantNotFoundException extends RestaurantException {

    public RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
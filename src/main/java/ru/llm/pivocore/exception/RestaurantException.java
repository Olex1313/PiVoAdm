package ru.llm.pivocore.exception;

public class RestaurantException extends RuntimeException {

    public RestaurantException() {
        super();
    }

    public RestaurantException(String message) {
        super(message);
    }

    public RestaurantException(String message, Throwable cause) {
        super(message,cause);
    }
}

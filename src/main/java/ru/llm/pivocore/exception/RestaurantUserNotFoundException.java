package ru.llm.pivocore.exception;

public class RestaurantUserNotFoundException extends RuntimeException{
    public RestaurantUserNotFoundException(String message) {
        super(message);
    }
}

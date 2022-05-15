package ru.llm.pivocore.exception;

public class RestaurantUserServiceException extends RuntimeException{

    public RestaurantUserServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestaurantUserServiceException(String formatted) {

    }
}

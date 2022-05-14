package ru.llm.pivocore.exception;

public class RestaurantUserServiceException extends RuntimeException{
    public RestaurantUserServiceException() {
        super();
    }


    public RestaurantUserServiceException(String message) {
        super(message);
    }

    public RestaurantUserServiceException(String message, Throwable cause) {
        super(message, cause);
    }


}

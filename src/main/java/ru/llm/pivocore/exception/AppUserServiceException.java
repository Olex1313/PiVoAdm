package ru.llm.pivocore.exception;

public class AppUserServiceException extends RuntimeException {

    public AppUserServiceException() {
        super();
    }

    public AppUserServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}

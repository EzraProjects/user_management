package com.phegondev.usersmanagementsystem.errorHandlers;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String errorMessage;

    public CustomException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

}

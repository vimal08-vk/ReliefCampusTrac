package com.example.reliefcamp.exception;

public class CampCapacityExceededException extends RuntimeException {
    public CampCapacityExceededException(String message) {
        super(message);
    }
}

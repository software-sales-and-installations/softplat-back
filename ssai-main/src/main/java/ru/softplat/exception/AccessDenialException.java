package ru.softplat.exception;

public class AccessDenialException extends RuntimeException {
    public AccessDenialException(String message) {
        super(message);
    }
}

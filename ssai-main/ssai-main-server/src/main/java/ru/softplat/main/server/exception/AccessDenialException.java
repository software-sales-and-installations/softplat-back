package ru.softplat.main.server.exception;

public class AccessDenialException extends RuntimeException {
    public AccessDenialException(String message) {
        super(message);
    }
}

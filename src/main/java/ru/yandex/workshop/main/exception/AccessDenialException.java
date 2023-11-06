package ru.yandex.workshop.main.exception;

public class AccessDenialException extends RuntimeException {
    public AccessDenialException(String message) {
        super(message);
    }
}

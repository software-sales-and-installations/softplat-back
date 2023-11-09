package ru.yandex.workshop.security.exception;

public class WrongRegException extends RuntimeException {
    public WrongRegException(String message) {
        super(message);
    }
}

package ru.yandex.workshop.main.exception;

public class NotValidValueException extends RuntimeException {
    public NotValidValueException(String message) {
        super(message);
    }
}

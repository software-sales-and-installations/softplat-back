package ru.yandex.workshop.main.exception;

public class WrongConditionException extends RuntimeException {
    public WrongConditionException(String message) {
        super(message);
    }
}

package ru.softplat.exception;

public class WrongConditionException extends RuntimeException {
    public WrongConditionException(String message) {
        super(message);
    }
}

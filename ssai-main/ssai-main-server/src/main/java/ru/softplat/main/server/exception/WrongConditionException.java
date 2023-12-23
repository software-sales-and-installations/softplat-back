package ru.softplat.main.server.exception;

public class WrongConditionException extends RuntimeException {
    public WrongConditionException(String message) {
        super(message);
    }
}

package ru.yandex.workshop.security.exception;

public class WrongDataDbException extends java.sql.SQLException {
    public WrongDataDbException(String message) {
        super(message);
    }
}

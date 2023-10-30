package ru.yandex.workshop.main.exception.handler;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

}

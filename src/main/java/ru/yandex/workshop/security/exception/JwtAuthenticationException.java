package ru.yandex.workshop.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private final HttpStatus httpStatus;

    public JwtAuthenticationException(String msg, HttpStatus status) {
        super(msg);
        this.httpStatus = status;
    }
}

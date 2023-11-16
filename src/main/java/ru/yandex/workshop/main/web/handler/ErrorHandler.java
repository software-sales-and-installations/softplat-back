package ru.yandex.workshop.main.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.workshop.main.dto.error.ErrorResponse;
import ru.yandex.workshop.main.exception.*;
import ru.yandex.workshop.security.exception.UnauthorizedException;
import ru.yandex.workshop.security.exception.WrongDataDbException;
import ru.yandex.workshop.security.exception.WrongRegException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateException(final DuplicateException e) {
        e.printStackTrace();
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongConditionException(final WrongConditionException e) {
        e.printStackTrace();
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final EntityNotFoundException e) {
        e.printStackTrace();
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongRegException(final WrongRegException e) {
        e.printStackTrace();
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnauthorizedException(final UnauthorizedException e) {
        e.printStackTrace();
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(final MethodArgumentNotValidException e) {
        e.printStackTrace();
        List<String> details = new ArrayList<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        log.error(details.toString());
        return new ErrorResponse(details.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAccessDenialException(final AccessDenialException e) {
        e.printStackTrace();
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAccessDenialException(final ImageUploadingError e) {
        e.printStackTrace();
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDbException(final WrongDataDbException e) {
        e.printStackTrace();
        log.error(Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }
}

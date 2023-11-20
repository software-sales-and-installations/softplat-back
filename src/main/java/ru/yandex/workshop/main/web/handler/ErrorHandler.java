package ru.yandex.workshop.main.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.yandex.workshop.main.dto.error.ErrorResponse;
import ru.yandex.workshop.main.exception.*;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.security.exception.UnauthorizedException;
import ru.yandex.workshop.security.exception.WrongDataDbException;
import ru.yandex.workshop.security.exception.WrongRegException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateException(final DuplicateException e) {
        e.printStackTrace();
        log.error(Arrays.toString(e.getStackTrace()));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.DUPLICATE_EXCEPTION.label)
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongConditionException(final WrongConditionException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.WRONG_CONDITION_EXCEPTION.label)
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final EntityNotFoundException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongRegException(final WrongRegException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.WRONG_CONDITION_EXCEPTION.label)
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnauthorizedException(final UnauthorizedException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.UNAUTHORIZED_EXCEPTION.label)
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(final MethodArgumentNotValidException e) {
        e.printStackTrace();
        List<String> details = new ArrayList<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.VALIDATION_EXCEPTION.label)
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAccessDenialException(final AccessDenialException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.ACCESS_EXCEPTION.label)
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleImageServerUploadException(final ImageServerUploadException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.IMAGE_SERVER_UPLOAD_EXCEPTION.label)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleImageFormatException(final ImageFormatException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.IMAGE_FORMAT_EXCEPTION.label)
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDbException(final WrongDataDbException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.DATA_EXCEPTION.label)
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMaxSizeException(final MaxUploadSizeExceededException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(ExceptionMessage.SIZE_EXCEPTION.label)
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }
}

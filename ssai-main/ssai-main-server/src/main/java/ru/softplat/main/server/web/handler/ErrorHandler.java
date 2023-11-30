package ru.softplat.main.server.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.softplat.main.dto.error.ErrorResponse;
import ru.softplat.main.server.exception.AccessDenialException;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.exception.WrongConditionException;

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
                .error(e.getClass().getSimpleName())
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
                .error(e.getClass().getSimpleName())
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
                .error(e.getClass().getSimpleName())
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> handleValidationErrors(final MethodArgumentNotValidException e) {
        e.printStackTrace();
        List<ErrorResponse> details = new ArrayList<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            details.add(ErrorResponse.builder()
                    .message(error.getDefaultMessage())
                    .error(error.getClass().getSimpleName())
                    .status(HttpStatus.BAD_REQUEST.toString())
                    .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                    .build());
        }
        log.error(String.valueOf(e));
        return details;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAccessDenialException(final AccessDenialException e) {
        e.printStackTrace();
        log.error(String.valueOf(e));
        return ErrorResponse.builder()
                .message(e.getMessage())
                .error(e.getClass().getSimpleName())
                .status(HttpStatus.CONFLICT.toString())
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
                .error(e.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .build();
    }
}

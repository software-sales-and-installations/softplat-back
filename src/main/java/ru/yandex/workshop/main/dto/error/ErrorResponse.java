package ru.yandex.workshop.main.dto.error;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ErrorResponse {
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}

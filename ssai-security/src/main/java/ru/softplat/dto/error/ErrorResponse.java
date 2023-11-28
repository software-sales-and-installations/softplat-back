package ru.softplat.dto.error;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ErrorResponse {
    private String message;
    private String error;
    private String status;
    private LocalDateTime timestamp;
}

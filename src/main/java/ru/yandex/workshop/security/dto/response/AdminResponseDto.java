package ru.yandex.workshop.security.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminResponseDto {
    private Long id;
    private String email;
    private String name;
}

package ru.softplat.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.security.model.Role;
import ru.yandex.workshop.security.model.Status;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
    Long id;
    String email;
    Role role;
    Status status;
}

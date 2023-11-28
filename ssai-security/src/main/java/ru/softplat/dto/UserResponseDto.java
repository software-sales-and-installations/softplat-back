package ru.softplat.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.softplat.model.Role;
import ru.softplat.model.Status;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
    Long id;
    String email;
    Role role;
    Status status;
}

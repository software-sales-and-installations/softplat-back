package ru.softplat.security.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.softplat.security.dto.model.Role;
import ru.softplat.security.dto.model.Status;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateMainDto {

    String email;
    String name;
    String phone;
    Role role;
    Status status;

}

package ru.softplat.main.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateMainDto {

    String email;
    String name;
    String phone;
}

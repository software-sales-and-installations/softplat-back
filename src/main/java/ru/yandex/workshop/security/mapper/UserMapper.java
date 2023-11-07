package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.dto.UserResponseDto;
import ru.yandex.workshop.security.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "email", source = "userDto.email")
    @Mapping(target = "password", source = "userDto.password")
    @Mapping(target = "role", source = "userDto.role")
    @Mapping(target = "status", source = "userDto.status")
    User userDtoToUser(UserDto userDto);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "status", source = "user.status")
    UserResponseDto userToUserResponseDto(User user);
}

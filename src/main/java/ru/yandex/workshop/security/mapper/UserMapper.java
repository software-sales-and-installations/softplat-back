package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.dto.UserResponseDto;
import ru.yandex.workshop.security.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser(UserDto userDto);

    UserResponseDto userToUserResponseDto(User user);
}

package ru.softplat.security.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.security.dto.UserCreateMainDto;
import ru.softplat.security.server.dto.UserCreateDto;
import ru.softplat.security.server.dto.UserResponseDto;
import ru.softplat.security.server.model.User;


@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User userDtoToUser(UserCreateDto userCreateDto);

    UserCreateMainDto userToUserMain(UserCreateDto userCreateDto);

    UserResponseDto userToUserResponseDto(User user);
}

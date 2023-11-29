package ru.softplat.security.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.security.server.dto.UserCreateDto;
import ru.softplat.dto.UserResponseDto;
import ru.softplat.security.server.model.User;


@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User userDtoToUser(UserCreateDto userCreateDto);

//    Admin userDtoToAdmin(UserCreateDto userCreateDto);
//
//    Seller userDtoToSeller(UserCreateDto userCreateDto);
//
//    Buyer userDtoToBuyer(UserCreateDto userCreateDto);

    UserResponseDto userToUserResponseDto(User user);
}

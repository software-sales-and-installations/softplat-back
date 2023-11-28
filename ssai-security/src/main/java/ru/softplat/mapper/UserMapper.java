package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.dto.UserCreateDto;
import ru.softplat.dto.UserResponseDto;
import ru.softplat.model.User;


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

package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.model.admin.Admin;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.dto.UserResponseDto;
import ru.yandex.workshop.security.model.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User userDtoToUser(UserDto userDto);

    Admin userDtoToAdmin(UserDto userDto);

    Seller userDtoToSeller(UserDto userDto);

    Buyer userDtoToBuyer(UserDto userDto);

    UserResponseDto userToUserResponseDto(User user);

    UserResponseDto userToUserResponseDto(Admin user);

    UserResponseDto userToUserResponseDto(Seller user);

    UserResponseDto userToUserResponseDto(Buyer user);
}

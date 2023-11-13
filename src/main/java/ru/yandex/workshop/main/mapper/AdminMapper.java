package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.user.response.AdminResponseDto;
import ru.yandex.workshop.main.model.admin.Admin;
import ru.yandex.workshop.security.dto.UserDto;

@Mapper(componentModel = "spring")
@Component
public interface AdminMapper {
    Admin userDtoToAdmin(UserDto userDto);

    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

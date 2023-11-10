package ru.yandex.workshop.main.dto.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.user.response.AdminResponseDto;
import ru.yandex.workshop.main.model.admin.Admin;
import ru.yandex.workshop.security.dto.UserDto;

@Mapper
public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    Admin userDtoToAdmin(UserDto userDto);

    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

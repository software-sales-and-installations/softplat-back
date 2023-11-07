package ru.yandex.workshop.main.dto.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.user.response.AdminResponseDto;
import ru.yandex.workshop.main.model.admin.Admin;
import ru.yandex.workshop.security.dto.UserDto;

@Mapper
public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    @Mapping(target = "email", source = "userDto.email")
    @Mapping(target = "name", source = "userDto.name")
    Admin userDtoToAdmin(UserDto userDto);

    @Mapping(target = "id", source = "admin.id")
    @Mapping(target = "email", source = "admin.email")
    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

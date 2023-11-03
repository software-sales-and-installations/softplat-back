package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.security.dto.response.AdminResponseDto;
import ru.yandex.workshop.security.dto.user.AdminDto;
import ru.yandex.workshop.security.model.Admin;

public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    @Mapping(target = "email", source = "adminDto.email")
    @Mapping(target = "name", source = "adminDto.email")
    Admin adminDtoToAdmin(AdminDto adminDto);

    @Mapping(target = "id", source = "admin.id")
    @Mapping(target = "email", source = "admin.email")
    @Mapping(target = "name", source = "admin.email")
    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

package ru.yandex.workshop.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.security.dto.registration.RegistrationAdminDto;
import ru.yandex.workshop.security.dto.response.AdminResponseDto;
import ru.yandex.workshop.security.model.user.Admin;

@Mapper
public interface AdminMapper {
    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    @Mapping(target = "email", source = "registrationAdminDto.email")
    @Mapping(target = "name", source = "registrationAdminDto.name")
    @Mapping(target = "password", source = "registrationAdminDto.password")
    @Mapping(target = "role", source = "registrationAdminDto.role")
    @Mapping(target = "status", source = "registrationAdminDto.status")
    Admin adminDtoToAdmin(RegistrationAdminDto registrationAdminDto);

    @Mapping(target = "id", source = "admin.id")
    @Mapping(target = "email", source = "admin.email")
    @Mapping(target = "name", source = "admin.name")
    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

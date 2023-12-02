package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.user.response.AdminResponseDto;
import ru.softplat.main.server.model.admin.Admin;
import ru.softplat.security.dto.UserCreateMainDto;

@Mapper
@Component
public interface AdminMapper {
    Admin adminFromUserCreateMainDto(UserCreateMainDto userCreateMainDto);

    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

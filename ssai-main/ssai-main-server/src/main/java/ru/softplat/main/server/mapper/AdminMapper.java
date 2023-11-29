package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.server.model.admin.Admin;
import ru.softplat.dto.user.response.AdminResponseDto;

@Mapper
@Component
public interface AdminMapper {
    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

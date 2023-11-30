package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.user.response.AdminResponseDto;
import ru.softplat.main.server.model.admin.Admin;

@Mapper
@Component
public interface AdminMapper {
    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

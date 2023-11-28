package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.dto.user.response.AdminResponseDto;
import ru.softplat.model.admin.Admin;

@Mapper
@Component
public interface AdminMapper {
    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

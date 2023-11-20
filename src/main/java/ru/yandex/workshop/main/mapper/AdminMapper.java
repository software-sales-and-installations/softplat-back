package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.user.response.AdminResponseDto;
import ru.yandex.workshop.main.model.admin.Admin;

@Mapper
@Component
public interface AdminMapper {
    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

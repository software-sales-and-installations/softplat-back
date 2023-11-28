package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AdminMapper {
    AdminResponseDto adminToAdminResponseDto(Admin admin);
}

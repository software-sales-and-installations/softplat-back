package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.seller.BankRequisitesCreateUpdateDto;
import ru.softplat.main.dto.seller.BankRequisitesResponseDto;
import ru.softplat.main.server.model.seller.BankRequisites;

@Mapper
@Component
public interface BankRequisitesMapper {
    BankRequisitesResponseDto requisitesToDto(BankRequisites requisites);

    BankRequisites createUpdateDtoToRequisites(BankRequisitesCreateUpdateDto createUpdateDto);
}
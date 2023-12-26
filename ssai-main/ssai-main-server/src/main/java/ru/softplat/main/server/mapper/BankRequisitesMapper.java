package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.seller.BankRequisitesCreateUpdateDto;
import ru.softplat.main.dto.seller.BankRequisitesResponseDto;
import ru.softplat.main.dto.seller.LegalForm;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.model.seller.BankRequisites;

@Mapper
@Component
public interface BankRequisitesMapper {
    BankRequisitesResponseDto requisitesToDto(BankRequisites requisites);

    BankRequisites createUpdateDtoToRequisites(BankRequisitesCreateUpdateDto createUpdateDto);

    default BankRequisites updateRequisites(BankRequisites oldRequisites, BankRequisites newRequisites) {
        if (newRequisites.getInn() != null) oldRequisites.setInn(newRequisites.getInn());
        if (newRequisites.getLegalForm() != null) {
            oldRequisites.setLegalForm(newRequisites.getLegalForm());
            if (oldRequisites.getLegalForm() == LegalForm.IP) oldRequisites.setOgrn(null);
            else oldRequisites.setOgrnip(null);
        }
        if (newRequisites.getAccount() != null) oldRequisites.setAccount(newRequisites.getAccount());
        if (newRequisites.getBik() != null) oldRequisites.setBik(newRequisites.getBik());
        if (newRequisites.getKpp() != null) oldRequisites.setKpp(newRequisites.getKpp());
        if (newRequisites.getOgrn() != null) oldRequisites.setOgrn(newRequisites.getOgrn());
        if (newRequisites.getOgrnip() != null) oldRequisites.setOgrnip(newRequisites.getOgrnip());
        if (newRequisites.getAddress() != null) oldRequisites.setAddress(newRequisites.getAddress());

        if (oldRequisites.getLegalForm() == LegalForm.IP) {
            if (oldRequisites.getInn().length() != 12)
                throw new WrongConditionException("Длина ИНН должна быть 12 цифр");
            if (oldRequisites.getOgrnip() == null || oldRequisites.getOgrnip().isBlank())
                throw new WrongConditionException("Введите корректный ОГРНИП");
        } else {
            if (oldRequisites.getInn().length() != 10)
                throw new WrongConditionException("Длина ИНН должна быть 10 цифр");
            if (oldRequisites.getOgrn() == null || oldRequisites.getOgrn().isBlank())
                throw new WrongConditionException("Введите корректный ОГРН");
        }

        return oldRequisites;
    }
}
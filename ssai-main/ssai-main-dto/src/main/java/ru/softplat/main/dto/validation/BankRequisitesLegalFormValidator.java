package ru.softplat.main.dto.validation;

import ru.softplat.main.dto.seller.BankRequisitesCreateUpdateDto;
import ru.softplat.main.dto.seller.LegalForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BankRequisitesLegalFormValidator implements ConstraintValidator<BankRequisitesLegalForm,
        BankRequisitesCreateUpdateDto> {
    @Override
    public boolean isValid(BankRequisitesCreateUpdateDto requisites, ConstraintValidatorContext context) {
        if (requisites.getLegalForm() == LegalForm.IP) {
            if (requisites.getInn().length() != 12) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Длина ИНН должна быть 12 цифр")
                        .addPropertyNode("inn").addConstraintViolation();
                return false;
            }
            if (requisites.getOgrnip() == null || requisites.getOgrnip().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Введите корректный ОГРНИП")
                        .addPropertyNode("ogrnip").addConstraintViolation();
                return false;
            }
        } else {
            if (requisites.getInn().length() != 10) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Длина ИНН должна быть 10 цифр")
                        .addPropertyNode("inn").addConstraintViolation();
                return false;
            }
            if (requisites.getOgrn() == null || requisites.getOgrn().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Введите корректный ОГРН")
                        .addPropertyNode("ogrn").addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
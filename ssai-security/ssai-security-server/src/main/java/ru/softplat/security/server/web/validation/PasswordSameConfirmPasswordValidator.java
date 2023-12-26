package ru.softplat.security.server.web.validation;


import ru.softplat.security.server.dto.UserCreateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordSameConfirmPasswordValidator implements ConstraintValidator<PasswordSameConfirmPassword, UserCreateDto> {
    @Override
    public boolean isValid(UserCreateDto value, ConstraintValidatorContext context) {
        return value.getConfirmPassword().equals(value.getPassword());
    }
}
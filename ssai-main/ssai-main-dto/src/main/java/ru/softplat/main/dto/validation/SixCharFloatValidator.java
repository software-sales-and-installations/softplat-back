package ru.softplat.main.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SixCharFloatValidator implements ConstraintValidator<SixCharFloat, Float> {

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext context) {
        if (value != null) {
            String stringValue = value.toString();
            return stringValue.length() <= 6;
        } else return true;
    }
}
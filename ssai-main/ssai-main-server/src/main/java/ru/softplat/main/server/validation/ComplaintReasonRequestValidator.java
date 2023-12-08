package ru.softplat.main.server.validation;

import ru.softplat.main.dto.compliant.ComplaintReasonRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ComplaintReasonRequestValidator implements ConstraintValidator<ValidComplaintReasonRequest, ComplaintReasonRequest> {
    @Override
    public boolean isValid(ComplaintReasonRequest value, ConstraintValidatorContext context) {
        return value != null;
    }
}

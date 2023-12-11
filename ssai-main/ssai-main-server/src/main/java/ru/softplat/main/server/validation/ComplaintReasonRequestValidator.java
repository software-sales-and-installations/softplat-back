package ru.softplat.main.server.validation;

import ru.softplat.main.dto.compliant.ComplaintReason;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ComplaintReasonRequestValidator implements ConstraintValidator<ValidComplaintReasonRequest, ComplaintReason> {
    @Override
    public boolean isValid(ComplaintReason value, ConstraintValidatorContext context) {
        return value != null;
    }
}

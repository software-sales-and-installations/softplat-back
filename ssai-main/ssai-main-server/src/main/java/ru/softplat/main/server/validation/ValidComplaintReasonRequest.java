package ru.softplat.main.server.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ComplaintReasonRequestValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidComplaintReasonRequest {
    String message() default "Invalid complaint reason request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

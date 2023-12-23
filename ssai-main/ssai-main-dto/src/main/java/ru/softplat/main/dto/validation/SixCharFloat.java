package ru.softplat.main.dto.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SixCharFloatValidator.class)
public @interface SixCharFloat {
    String message() default "Число должно быть не более 6 символов в длину";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
package ru.softplat.main.dto.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BankRequisitesLegalFormValidator.class)
public @interface BankRequisitesLegalForm {
    String message() default "Введите корректные реквизиты";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
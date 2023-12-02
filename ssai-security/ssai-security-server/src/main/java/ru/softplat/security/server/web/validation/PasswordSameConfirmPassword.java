package ru.softplat.security.server.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordSameConfirmPasswordValidator.class)
public @interface PasswordSameConfirmPassword {
    String message() default "Повторно указанный пароль не совпадает с паролем.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
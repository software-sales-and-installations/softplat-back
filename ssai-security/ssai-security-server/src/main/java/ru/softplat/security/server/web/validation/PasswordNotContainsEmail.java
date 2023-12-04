package ru.softplat.security.server.web.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordNotContainsEmailValidator.class)
public @interface PasswordNotContainsEmail {
    String message() default "Пароль не должен содержать email пользователя";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
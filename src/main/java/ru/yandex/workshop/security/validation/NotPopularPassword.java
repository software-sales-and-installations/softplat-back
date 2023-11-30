package ru.yandex.workshop.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotPopularPasswordValidator.class)
@Documented
public @interface NotPopularPassword {
    String message() default "Пароль слишком простой.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

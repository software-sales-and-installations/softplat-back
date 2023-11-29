package ru.softplat.main.server.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipartFileFormatValidator.class)
@Documented
public @interface MultipartFileFormat {
    String message() default "Недопустимый формат изображения.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package ru.yandex.workshop.security.dto;

import org.junit.jupiter.api.Test;
import ru.yandex.workshop.security.model.Role;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserCreateDtoTest {

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    public void testPassword_whenContainsEmail_thenValidatorWork() {
        UserCreateDto user = UserCreateDto.builder()
                .email("test@example.com")
                .password("test@examplePassword123!")
                .confirmPassword("test@examplePassword123!")
                .name("John Doe")
                .role(Role.BUYER)
                .build();

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<UserCreateDto> violation = violations.iterator().next();
        assertEquals("Пароль не должен содержать email пользователя", violation.getMessage());
    }

    @Test
    public void testPassword_whenNotSameConfirmPassword_thenValidatorWork() {
        UserCreateDto user = UserCreateDto.builder()
                .email("test@example.com")
                .password("Password123!")
                .confirmPassword("DifferentPassword123!")
                .name("John Doe")
                .role(Role.BUYER)
                .build();

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<UserCreateDto> violation = violations.iterator().next();
        assertEquals("Повторно указанный пароль не совпадает с паролем.", violation.getMessage());
    }

    @Test
    public void testPassword_whenPopularPassword_thenValidatorWork() {
        UserCreateDto user = UserCreateDto.builder()
                .email("test@example.com")
                .password("!Qwerty12345")
                .confirmPassword("!Qwerty12345")
                .name("John Doe")
                .role(Role.BUYER)
                .build();

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<UserCreateDto> violation = violations.iterator().next();
        assertEquals("Пароль слишком простой.", violation.getMessage());
    }
}
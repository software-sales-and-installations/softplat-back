package ru.softplat.security.server.web.validation;

import ru.softplat.security.server.exception.WrongRegException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class NotPopularPasswordValidator implements ConstraintValidator<NotPopularPassword, String> {
    private Set<String> invalidPasswords;

    @Override
    public void initialize(NotPopularPassword constraintAnnotation) {
        invalidPasswords = loadInvalidPasswords();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return !invalidPasswords.contains(password);
    }

    private Set<String> loadInvalidPasswords() {
        Set<String> invalidPasswords = new HashSet<>();
        try (InputStream inputStream = getClass().getResourceAsStream("/invalid-password-list.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                invalidPasswords.add(line.trim());
            }
        } catch (IOException e) {
            throw new WrongRegException("Проблемы с файлом");
        }

        return invalidPasswords;
    }
}
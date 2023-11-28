package ru.softplat.message;

public enum ExceptionMessage {
    NOT_VALID_TOKEN("Невалидный токен."),
    TIMEOUT_TOKEN("Время токена вышло."),
    INVALID_AUTHENTICATION("Неправильный email/password."),
    ENTITY_NOT_FOUND_EXCEPTION("Требуемая запись не найдена."),
    CONFIRMED_PASSWORD_EXCEPTION("Пароли не совпадают."),
    DUPLICATE_EXCEPTION("Пользователь с указанным логином уже существует.");

    public final String label;

    ExceptionMessage(String label) {
        this.label = label;
    }
}

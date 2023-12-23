package ru.softplat.security.server.message;

public enum ExceptionMessage {
    NOT_VALID_TOKEN("Невалидный токен."),
    TIMEOUT_TOKEN("Время токена вышло."),
    INVALID_AUTHENTICATION("Неправильный email/password."),
    ENTITY_NOT_FOUND_EXCEPTION("Требуемая запись %s не найдена."),
    CONFIRMED_PASSWORD_EXCEPTION("Пароли не совпадают."),
    IMAGE_SIZE_EXCEED_EXCEPTION("Размер изображения должен не превышать 5 мб."),
    IMAGE_FORMAT_EXCEPTION("Формат изображения должен быть jpeg или png."),
    NO_IMAGE_CONTENT_EXCEPTION("Необходимо загрузить изображение."),
    DUPLICATE_EXCEPTION("Пользователь с указанным логином уже существует."),
    COMPLAINT_REASON_EXCEPTION("Неверно указана причина жалобы."),
    IMAGE_SERVER_UPLOAD_EXCEPTION("Ошибка сервера при загрузке изображения.");

    public final String label;

    ExceptionMessage(String label) {
        this.label = label;
    }

    public String getMessage(String role) {
        return String.format(label, role);
    }
}

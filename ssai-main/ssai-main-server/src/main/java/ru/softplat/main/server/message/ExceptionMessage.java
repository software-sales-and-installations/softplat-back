package ru.softplat.main.server.message;

public enum ExceptionMessage {
    ENTITY_NOT_FOUND_EXCEPTION("Требуемая запись c ID (или email) %s и типом %s не найдена."),
    DUPLICATE_EXCEPTION("Данная запись уже существует "),
    IMAGE_SERVER_UPLOAD_EXCEPTION("Ошибка сервера при загрузке изображения."),
    NO_RIGHTS_EXCEPTION("Продавец не может корректировать чужой продукт!"),
    WRONG_CONDITION_EXCEPTION("Введены некорректные данные."),
    UNAUTHORIZED_EXCEPTION("Ошибка авторизации."),
    VALIDATION_EXCEPTION("Ошибка проверки данных."),
    ACCESS_EXCEPTION("Ошибка доступа"),
    DATA_EXCEPTION("Ошибка базы данных."),
    SIZE_EXCEPTION("Ошибка в размере загружаемых данных"),
    NOT_VALID_VALUE_EXCEPTION("Недопустимое значение!");

    public final String label;

    ExceptionMessage(String label) {
        this.label = label;
    }

    public String getMessage(long id, Class<?> entityClass) {
        String entityType = entityClass.getSimpleName();
        return String.format(label, id, entityType);
    }
}

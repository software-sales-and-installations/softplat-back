package ru.yandex.workshop.main.message;

public enum ExceptionMessage {
    ENTITY_NOT_FOUND_EXCEPTION("Требуемая запись не найдена."),
    DUPLICATE_EXCEPTION("Данная запись уже существует."),
    IMAGE_SIZE_EXCEED_EXCEPTION("Размер изображения должен не превышать 5 мб."),
    IMAGE_FORMAT_EXCEPTION("Формат изображения должен быть jpeg или png."),
    NO_IMAGE_CONTENT_EXCEPTION("Необходимо загрузить изображение."),
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
}

package ru.yandex.workshop.main.message;

public enum ExceptionMessage {
    ENTITY_NOT_FOUND_EXCEPTION("Требуемая запись не найдена."),
    DUPLICATE_EXCEPTION("Данная запись уже существует."),
    IMAGE_UPLOADING_ERROR("Ошибка при загрузке изображения"),
    NO_RIGHTS_EXCEPTION("Продавец не может корректировать чужой продукт!"),
    NOT_VALID_VALUE_EXCEPTION("Недопустимое значение!");

    public final String label;

    ExceptionMessage(String label) {
        this.label = label;
    }
}

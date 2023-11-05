package ru.yandex.workshop.main.message;

public enum ExceptionMessage {
    ENTITY_NOT_FOUND_EXCEPTION("Требуемая запись не найдена."),
    DUPLICATE_EXCEPTION("Данная запись уже существует.");
    public final String label;

    ExceptionMessage(String label) {
        this.label = label;
    }
}

package ru.yandex.workshop.main.message;

public enum ExceptionMessage {
    NOT_FOUND_VENDOR_EXCEPTION("Объект не найден или недоступен."),
    NOT_FOUND_COUNTRY_EXCEPTION("Объект не найден или недоступен.");
    public final String label;

    ExceptionMessage(String label) {
        this.label = label;
    }
}

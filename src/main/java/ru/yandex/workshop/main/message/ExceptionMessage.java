package ru.yandex.workshop.main.message;

public enum ExceptionMessage {
    NOT_FOUND_VENDOR_EXCEPTION("Вендор не найден или недоступен."),
    NOT_FOUND_COUNTRY_EXCEPTION("Страна не найдена или недоступена.");
    public final String label;

    ExceptionMessage(String label) {
        this.label = label;
    }
}

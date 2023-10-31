package ru.yandex.workshop.main.message;

public enum ExceptionMessage {
    NOT_FOUND_VENDOR_EXCEPTION("Вендор не найден или недоступен."),
    NOT_FOUND_COUNTRY_EXCEPTION("Страна не найдена или недоступна."),
    NOT_FOUND_USER_EXCEPTION("Пользователь не найден или недоступен."),
    NOT_PRODUCT_PRODUCT_EXCEPTION("Продукт не найден или недоступен.");


    public final String label;

    ExceptionMessage(String label) {
        this.label = label;
    }
}

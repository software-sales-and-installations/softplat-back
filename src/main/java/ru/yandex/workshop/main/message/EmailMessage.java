package ru.yandex.workshop.main.message;

public enum EmailMessage {
    ORDER_CONFIRM_SUBJECT("Подтверждение заказа №%d"),
    ORDER_CONFIRM_EMAIL("Заказ №%d\n\n" +
            "Дата: %s\n\n" +
            "Описание заказанных товаров\n%s\n" +
            "Имя покупателя: %s\n" +
            "Телефон покупателя: %s\n" +
            "Email покупателя: %s\n");

    // TODO ... сообщения сброса пароля / регистрации и т.д.

    public final String body;

    EmailMessage(String message) {
        this.body = message;
    }
}

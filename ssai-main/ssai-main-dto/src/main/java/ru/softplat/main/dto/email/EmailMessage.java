package ru.softplat.main.dto.email;

public enum EmailMessage {
    ORDER_CONFIRM_SUBJECT("Подтверждение заказа №%d"),
    ORDER_CONFIRM_EMAIL("Заказ №%d\n\n" +
            "Дата: %s\n\n" +
            "Описание заказанных товаров\n%s\n" +
            "Имя покупателя: %s\n" +
            "Телефон покупателя: %s\n" +
            "Email покупателя: %s\n"),
    REG_CONFIRM_SUBJECT("Подтверждение регистрации пользователя %s"),
    REG_CONFIRM_EMAIL("Привет, %s,\n\n" +
            "Вы были зарегистрированы на сайте softplat.ru\n\n" +
            "Дата: %s\n\n" +
            "Спасибо за регистрацию!"),
    RESTORE_PASSWORD_SUBJECT("Восстановление пароля"),
    RESTORE_PASSWORD_EMAIL("Ваша ссылка для восстановления пароля: \n\n" +
                              "%s\n\n" +
                              "Проигнорируйте это сообщение, если вы не отправляли заявку на восстановления пароля.");

    public final String body;

    EmailMessage(String message) {
        this.body = message;
    }
}
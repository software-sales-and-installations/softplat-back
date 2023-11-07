package ru.yandex.workshop.security.message;

public enum LogMessage {
    TRY_AUTHORIZATION("Попытка авторизации пользователя."),
    TRY_LOGOUT("Попытка выйти из учетной записи."),
    TRY_REGISTRATION("Попытка регистрации пользователя."),
    TRY_CHANGE_PASSWORD("Попытка изменить пароль.")
    ;

    public final String label;

    LogMessage(String label) {
        this.label = label;
    }
}

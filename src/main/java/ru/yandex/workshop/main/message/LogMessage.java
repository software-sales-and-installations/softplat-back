package ru.yandex.workshop.main.message;

public enum LogMessage {
    TRY_ADMIN_ADD_VENDOR("Попытка получения подборок событий.");
    public final String label;

    LogMessage(String label) {
        this.label = label;
    }
}

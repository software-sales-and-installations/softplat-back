package ru.yandex.workshop.main.message;

public enum LogMessage {
    TRY_ADMIN_ADD_VENDOR("Попытка добавления нового вендора."),
    TRY_ADMIN_PATCH_VENDOR("Попытка изменения вендора."),
    TRY_ADMIN_GET_VENDOR("Попытка получения списка вендоров."),
    TRY_ADMIN_GET_ID_VENDOR("Попытка получения вендора по id: {}."),
    TRY_ADMIN_DELETE_VENDOR("Попытка удаления вендора по id: {}."),
    ADMIN_ADD_VENDOR("Успешное добавление нового вендора."),
    ADMIN_PATCH_VENDOR("Успешное изменение вендора."),
    ADMIN_GET_VENDOR("Успешное получение списка вендоров."),
    ADMIN_GET_ID_VENDOR("Успешное получение вендора по id: {}."),
    ADMIN_DELETE_VENDOR("Успешное удаление вендора по id: {}.");
    public final String label;

    LogMessage(String label) {
        this.label = label;
    }
}

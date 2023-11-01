package ru.yandex.workshop.main.message;

public enum LogMessage {
    TRY_ADMIN_ADD_VENDOR("Попытка добавления нового вендора."),
    TRY_ADMIN_PATCH_VENDOR("Попытка изменения вендора."),
    TRY_ADMIN_GET_VENDOR("Попытка получения списка вендоров."),
    TRY_ADMIN_GET_ID_VENDOR("Попытка получения вендора по id: {}."),
    TRY_ADMIN_DELETE_VENDOR("Попытка удаления вендора по id: {}."),
    TRY_ADD_PRODUCT_IN_BASKET("Попытка добавления в корзину продукта с id {}."),
    TRY_CHECK_BASKET("Попытка просмотра корзины пользователем с id {}."),
    TRY_DELETE_PRODUCT_FROM_BASKET("Попытка удаление из корзины продукта с id {}."),
    ADMIN_ADD_VENDOR("Успешное добавление нового вендора."),
    ADMIN_PATCH_VENDOR("Успешное изменение вендора."),
    ADMIN_GET_VENDOR("Успешное получение списка вендоров."),
    ADMIN_GET_ID_VENDOR("Успешное получение вендора по id: {}."),
    ADMIN_DELETE_VENDOR("Успешное удаление вендора по id: {}."),
    ADD_PRODUCT_IN_BASKET("Успешное добавление в корзину продукта с id {}."),
    CHECK_BASKET("Успешный просмотр корзины пользователем с id {}."),
    DELETE_PRODUCT_FROM_BASKET("Успешное удаление из корзины продукта с id {}.");
    public final String label;

    LogMessage(String label) {
        this.label = label;
    }
}

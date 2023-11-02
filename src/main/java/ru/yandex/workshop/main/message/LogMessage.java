package ru.yandex.workshop.main.message;

public enum LogMessage {
    TRY_ADD_BUYER("Попытка добавления нового покупателя."),
    TRY_PATCH_BUYER("Попытка изменения покупателя по id: {}"),
    TRY_GET_BUYER("Попытка получения покупателя по id: {}"),
    TRY_ADD_SELLER("Попытка добавления нового продавца."),
    TRY_PATCH_SELLER("Попытка изменения продавца по email: {}"),
    TRY_GET_SELLER("Попытка получения продавца по email: {}"),
    TRY_SELLER_PATCH_REQUISITES("Попытка изменения реквизитов по email: {}"),
    TRY_SELLER_GET_REQUISITES("Попытка получения реквизитов по email: {}"),
    TRY_SELLER_DELETE_REQUISITES("Попытка удаления реквизитов по email: {}"),
    TRY_ADMIN_ADD_VENDOR("Попытка добавления нового вендора."),
    TRY_ADMIN_PATCH_VENDOR("Попытка изменения вендора."),
    TRY_GET_VENDOR("Попытка получения списка вендоров."),
    TRY_GET_ID_VENDOR("Попытка получения вендора по id: {}."),
    TRY_ADMIN_DELETE_VENDOR("Попытка удаления вендора по id: {}."),
    TRY_ADD_PRODUCT_IN_BASKET("Попытка добавления в корзину продукта с id {}."),
    TRY_CHECK_BASKET("Попытка просмотра корзины пользователем с id {}."),
    TRY_DELETE_PRODUCT_FROM_BASKET("Попытка удаление из корзины продукта с id {}.");

    public final String label;

    LogMessage(String label) {
        this.label = label;
    }
}

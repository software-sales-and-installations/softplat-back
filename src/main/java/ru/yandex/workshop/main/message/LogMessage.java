package ru.yandex.workshop.main.message;

public enum LogMessage {
    TRY_PATCH_BUYER("Попытка изменения покупателя по email: {}"),
    TRY_GET_BUYER("Попытка получения покупателя по id: {}"),
    TRY_PATCH_SELLER("Попытка изменения продавца по email: {}"),
    TRY_GET_SELLER("Попытка получения продавца по id: {}"),
    TRY_GET_All_SELLERS("Попытка получения списка продавцов"),
    TRY_SELLER_PATCH_REQUISITES("Попытка изменения реквизитов по email: {}"),
    TRY_SELLER_GET_REQUISITES("Попытка получения реквизитов по id продавца: {}"),
    TRY_SELLER_DELETE_REQUISITES("Попытка удаления реквизитов по email: {}"),
    TRY_ADMIN_ADD_VENDOR("Попытка добавления нового вендора."),
    TRY_ADMIN_PATCH_VENDOR("Попытка изменения вендора."),
    TRY_GET_VENDOR("Попытка получения списка вендоров."),
    TRY_GET_ID_VENDOR("Попытка получения вендора по id: {}."),
    TRY_ADMIN_DELETE_VENDOR("Попытка удаления вендора по id: {}."),
    TRY_GET_ADMIN("Попытка получения админа по email: {}"),
    TRY_UPDATE_STATUS_PRODUCT_ON_PUBLISHED("Попытка установки статуса 'PUBLISHED' продукту id: {}."),
    TRY_UPDATE_STATUS_PRODUCT_ON_REJECTED("Попытка установки статуса 'REJECTED' продукту id: {}."),
    TRY_DELETE_PRODUCT("Попытка удаления продукта по id: {}."),
    TRY_GET_PRODUCTS_OF_SELLER("Попытка получения всех своих продуктов продавца с id: {}."),
    TRY_GET_PRODUCT_BY_ID("Попытка получения продукта id: {} продавца id: {}."),
    TRY_CREATE_PRODUCT("Попытка создать продукт: {}."),
    TRY_UPDATE_PRODUCT("Попытка обновления продукта id: {} продавцом email: {}: {}"),
    TRY_UPDATE_STATUS_PRODUCT_ON_SENT("Попытка установки статуса 'SHIPPED' продукту id: {} продавцом: {}."),
    TRY_ADMIN_ADD_CATEGORY("Попытка добавить категорию."),
    TRY_ADMIN_PATCH_CATEGORY("Попытка изменения категории по id: {}"),
    TRY_GET_CATEGORY("Попытка получения списка категорий."),
    TRY_GET_ALL_PRODUCTS_SHIPPED("Попытка получения всех продуктов, ожидающих модерации."),
    TRY_GET_ID_CATEGORY("Попытка получения категории по id: {}."),
    TRY_ADMIN_DELETE_CATEGORY("Попытка удаления категории по id: {}."),
    TRY_ADD_IMAGE("Попытка добавления изображения"),
    TRY_DElETE_IMAGE("Попытка удаления изображения"),
    TRY_ADD_PRODUCT_IN_BASKET("Попытка добавления в корзину продукта с id {}."),
    TRY_CHECK_BASKET("Попытка просмотра корзины пользователем с id {}."),
    TRY_DELETE_PRODUCT_FROM_BASKET("Попытка удаление из корзины продукта с id {}."),
    TRY_ADD_ADMIN("ПОпытка добавить нового админа"),
    TRY_ADD_SELLER("ПОпытка добавить нового продавца"),
    TRY_ADD_BUYER("ПОпытка добавить нового продавца"),
    TRY_BUYER_ADD_FAVORITE("Попытка добавления данных в избранное"),
    TRY_BUYER_DELETE_FAVORITE("Попытка удаления данных из избранного"),
    TRY_BUYER_GET_FAVORITE("Попытка получения данных избранного по id покупателя: {}");

    public final String label;

    LogMessage(String label) {
        this.label = label;
    }
}

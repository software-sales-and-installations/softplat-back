package ru.softplat.security.server.message;

public enum LogMessage {
    TRY_PATCH_BUYER("Попытка изменения покупателя по id: {}"),
    TRY_GET_BUYER("Попытка получения покупателя по id: {}"),
    TRY_ADD_SELLER("Попытка добавления нового продавца."),
    TRY_GET_SIMILAR_PRODUCTS("Попытка получения списка похожих продуктов."),
    TRY_GET_PRODUCTS_FILTER("Попытка получения списка продуктов с учетом фильтра."),
    TRY_PATCH_SELLER("Попытка изменения продавца по id: {}"),
    TRY_GET_SELLER("Попытка получения продавца по id: {}"),
    TRY_GET_All_SELLERS("Попытка получения списка продавцов"),
    TRY_DELETE_SELLER("Попытка удаления продавца"),
    TRY_SELLER_PATCH_REQUISITES("Попытка изменения реквизитов по id: {}"),
    TRY_SELLER_GET_REQUISITES("Попытка получения реквизитов по id продавца: {}"),
    TRY_SELLER_DELETE_REQUISITES("Попытка удаления реквизитов по id: {}"),
    TRY_ADMIN_ADD_VENDOR("Попытка добавления нового вендора."),
    TRY_ADMIN_PATCH_VENDOR("Попытка изменения вендора."),
    TRY_GET_VENDORS("Попытка получения списка вендоров."),
    TRY_GET_ID_VENDOR("Попытка получения вендора по id: {}."),
    TRY_ADMIN_DELETE_VENDOR("Попытка удаления вендора по id: {}."),
    TRY_GET_ADMIN("Попытка получения админа по id: {}"),
    TRY_UPDATE_STATUS_PRODUCT("Попытка установки статуса {} продукту id: {}."),
    TRY_DELETE_PRODUCT("Попытка удаления продукта по id: {}."),
    TRY_GET_PRODUCTS_OF_SELLER("Попытка получения всех своих продуктов продавца с id: {}."),
    TRY_GET_PRODUCT_BY_ID("Попытка получения продукта id: {} продавца id: {}."),
    TRY_CREATE_PRODUCT("Попытка создать продукт: {}."),
    TRY_UPDATE_PRODUCT("Попытка обновления продукта id: {} продавцом id: {}: {}"),
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
    TRY_DELETE_PRODUCT_FROM_BASKET("Попытка удаления из корзины продукта с id {}."),
    TRY_DELETE_BASKET_POSITION("Попытка удаления из корзины позиции с id {}."),
    TRY_CLEAR_BASKET("Попытка очистки корзины пользователем с id {}."),
    TRY_ADD_ADMIN("Попытка добавить нового админа"),
    TRY_ADD_BUYER("Попытка добавить нового покупателя"),
    TRY_DELETE_BUYER("Попытка удалить покупателя"),
    TRY_BUYER_ADD_FAVORITE("Попытка добавления данных в избранное"),
    TRY_BUYER_DELETE_FAVORITE("Попытка удаления данных из избранного"),
    TRY_BUYER_GET_FAVORITE("Попытка получения данных избранного по id покупателя: {}"),
    TRY_ADD_ORDER("Попытка создания оплаченного заказа"),
    TRY_GET_ALL_ORDERS("Попытка получить все покупки"),
    TRY_GET_ORDER("Попытка получить заказ по id {}"),
    TRY_BUYER_GET_RECOMMENDATIONS("Попытка получения рекомендаций по id покупателя: {}"),
    TRY_GET_All_BUYERS("Попытка получения всех покупателей"),
    TRY_GET_STATS_SELLER_ADMIN("Попытка получения статистики по продавцам админом"),
    TRY_GET_STATS_PRODUCT_ADMIN("Попытка получения статистики по продукту админом"),
    TRY_GET_STATS_PRODUCT_SELLER("Попытка получения статистики по продукту продавцом"),
    TRY_CREATE_COMMENT("Попытка опубликовать комментарий к товару покупателем: {}"),
    TRY_UPDATE_COMMENT("Попытка обновления комментария к товару покупателем: {}"),
    TRY_DELETE_COMMENT_BUYER("Попытка удаления комментария к товару покупателем"),
    TRY_DELETE_COMMENT_ADMIN("Попытка удаления комментария к товару администратором"),
    TRY_GET_COMMENT_PUBLIC("Попытка просмотра комментария к товару пользователем"),
    TRY_GET_ALL_COMMENTS_PUBLIC("Попытка просмотра всех комментариев к товару пользователем"),
    TRY_AUTHORIZATION("Попытка авторизации пользователя email:{}"),
    TRY_LOGOUT("Попытка выйти из приложения"),
    TRY_REGISTRATION("Попытка зарегистрировать пользователя"),
    TRY_CHANGE_PASSWORD("Попытка сменить пароль");


    public final String label;

    LogMessage(String label) {
        this.label = label;
    }
}

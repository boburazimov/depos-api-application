package uz.depos.app.domain.enums;

/**
 * Способ регистрации/авторизации пользователя
 */

public enum UserAuthTypeEnum {
    ERI, // Только ERI (e-imzo)
    INPASS, // Только логин/пароль
    ANY, // ONE-ID или логин/пароль
    DISABLED, // Отключена
}

package uz.depos.app.domain.enums;

/**
 * Способ регистрации/авторизации пользователя
 */

public enum UserAuthTypeEnum {
    ONEID, // Только ONE-ID
    INNPASS, // Только логин/пароль
    ANY, // ONE-ID или логин/пароль
    DISABLED, // Отключена
}

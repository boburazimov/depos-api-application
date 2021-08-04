package uz.depos.app.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    // Администратор системный рол. Все - Полные права (Супер пользователь)
    public static final String ADMIN = "ROLE_ADMIN";

    // Пользователь - обычный*/
    public static final String USER = "ROLE_USER";

    // Модератор - системный рол с ограниченными возможностями
    public static final String MODERATOR = "ROLE_MODERATOR";

    // Наблюдатель - пользователь с ограниченными возможностями
    public static final String OBSERVER = "ROLE_OBSERVER";

    // Председатель наб. совета - пользователь с ограниченными возможностями
    public static final String CHAIRMAN = "ROLE_CHAIRMAN";

    // Секретарь наб. совета - пользователь с ограниченными возможностями
    public static final String SECRETARY = "ROLE_SECRETARY";

    // Анонимный пользователь
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}

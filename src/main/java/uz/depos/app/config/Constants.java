package uz.depos.app.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    // Regex for acceptable INN
    public static final String INN_REGEX = "^((?:[A-Z]{3})?\\d{9})$";

    // Regex for acceptable PINFL
    public static final String PINFL_REGEX = "^((?:[A-Z]{3})?\\d{14})$";

    // Shared-salt - key word for encode/decode password between Back and Frond
    public static final String PASSWORD_KEY = "1q#ESDwp";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "ru";

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_SIZE = "10";
    public static final int MAX_SIZE = 20;
    public static final String DEFAULT_BEGIN_DATE = "1970-01-01 01:00:49.841000";
    public static final String DEFAULT_END_DATE = "2100-01-01 01:00:49.841000";

    private Constants() {}
}

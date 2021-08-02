package uz.depos.app.domain.enums;

/**
 * Статус собрание
 */

public enum MeetingStatusEnum {
    PENDING, // Ожидает запуска
    ACTIVE, // Активно по расписанию
    FINISH, // Завершено (в архиве)
    CANCELED, // Отменено (в архиве)
    DISABLED, // Заблокировано/Неактивно
}

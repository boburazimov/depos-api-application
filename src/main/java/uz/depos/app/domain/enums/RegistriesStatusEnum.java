package uz.depos.app.domain.enums;

/**
 * Статус загрузки реестра для заседания (список участников - файл Excel)
 */

public enum RegistriesStatusEnum {
    LOADED, // Загружен
    PROCESSING, // Обработка
    COMPLETED, // Обработан
    ERROR, // Ошибка обработки
    CANCELED, // Отменен
}

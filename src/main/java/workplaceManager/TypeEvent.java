package workplaceManager;

public enum TypeEvent {
    USER_REGISTER("Регистрация пользователя"),
    USER_LOGIN("Вход пользователя"),
    USER_DELETE("Удаление пользователя"),

    ADD("Добавление"),
    UPDATE("Редактирование"),
    //UPDATE_MAIN_INFORMATION("Изменение основной информации"),
    //UPDATE_BINDING_ACCOUNTING1C("Изменение привязки к бухгалтерии"),
    //UPDATE_BINDING_WORKPLACE("Изменение привязки к рабочему месту"),
    //UPDATE_OS_COMPUTER("Изменение операционной системы"),
    //UPDATE_CONFIG_COMPUTER("Изменение конфигурации компьютера"),
    DELETE("Удаление"),

    ACCOUNTING1C_MOVING("Перемещение"),
    ACCOUNTING1C_CANCELLATION("Списание"),

    READ_CONFIG_COMPUTER("Считывание конфигурации компьютера");

    private String title;

    TypeEvent(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}

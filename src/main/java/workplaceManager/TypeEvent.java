package workplaceManager;

public enum TypeEvent {
    USER_REGISTER("Регистрация пользователя"),
    USER_LOGIN("Вход пользователя"),
    USER_DELETE("Удаление пользователя"),

    ADD("Добавление"),
    UPDATE("Редактирование"),
    READ_CONFIG_COMPUTER("Считывание конфигурации компьютера"),
    ACCOUNTING1C_MOVING("Перемещение"),

    ACCOUNTING1C_CANCELLATION("Списание"),
    DELETE("Удаление");

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

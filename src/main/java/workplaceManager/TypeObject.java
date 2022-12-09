package workplaceManager;

public enum TypeObject {
    USER ("Пользователь"),
    COMPUTER ("Компьютер"),
    MONITOR ("Монитор"),
    PRINTER ("Принтер"),
    SCANNER ("Сканер"),
    MFD ("МФУ"),
    UPS ("ИБП"),
    ACCOUNTING1C("Бухгалтерия"),
    EMPLOYEE("Сотрудник"),
    WORKPLACE("Рабочее место");

    private String title;

    TypeObject(String title) {
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

package workplaceManager;

public enum TypeParameter {
    UID("UID"),
    MANUFACTURER("Производитель"),
    MODEL("Модель"),
    COMMENT("Комментарий"),
    WORKPLACE("Рабочее место"),
    ACCOUNTING1C("Бухгалтерия"),
    IP ("IP"),
    NET_NAME("Сетевое имя"),
    OS("Операционная система"),
    MOTHERBOARD("Материнская плата"),
    PROCESSOR("Процессор"),
    RAM("Оперативная память"),
    HARDDRIVE("Жесткий диск"),
    VIDEOCARD("Видеокарта"),
    VIRTUAL_MACHINE("Виртуальные машины"),

    EMPLOYEE_USERNAME("ФИО"),
    EMPLOYEE_POST("Должность"),

    ACCOUNTING1C_TITLE("Название"),
    ACCOUNTING1C_INVENTORY_NUMBER("Инвентарный номер"),
    ACCOUNTING1C_EMPLOYEE("Сотрудник"),

    WORKPLACE_TITLE("Название");

    private String title;

    TypeParameter(String title) {
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

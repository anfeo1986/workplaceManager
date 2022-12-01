package workplaceManager;

public enum TypeParameter {
    UID("UID"),
    MANUFACTURER("Производитель"),
    MODEL("Модель"),
    WORKPLACE("Рабочее место"),
    ACCOUNTING1C("Бухгалтерия"),
    IP ("IP"),
    NET_NAME("Сетевое имя"),
    OS("Операционная система"),
    MOTHERBOARD("Материнская плата"),
    PROCESSOR("Процессоры");

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

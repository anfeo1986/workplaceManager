package workplaceManager.sorting;

public enum FilterAccounting1C {
    ALL("Все"),
    EQUIPMENT_NOT_HAVE("Нет оборудования"),
    EQUIPMENT_HAVE("Есть оборудование");
    private String title;

    FilterAccounting1C(String title) {
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

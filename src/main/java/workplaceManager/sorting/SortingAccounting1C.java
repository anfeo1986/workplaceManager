package workplaceManager.sorting;

public enum SortingAccounting1C {
    INVENTORY_NUMBER("По инвентарному номеру"),
    TITLE("По названию"),
    EMPLOYEE("По материально-ответственному лицу");

    private String title;

    SortingAccounting1C(String title) {
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

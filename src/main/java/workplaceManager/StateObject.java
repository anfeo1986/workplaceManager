package workplaceManager;

public enum StateObject {
    DELETED("Удаленный"),
    NO_DELETED("Неудаленный");

    private String title;

    StateObject(String title) {
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

package ru.softplat.main.dto.compliant;

public enum ComplaintStatus {
    OPENED("Жалобу еще не взяли в работу"),
    CLOSED("Жалоба отработана и закрыта"),
    REVIEWED_BY_SELLER("Жалоба отработана продавцом"),
    REVIEWED_BY_ADMIN("Жалоба отработана администратором");

    public final String label;

    ComplaintStatus(String label) {
        this.label = label;
    }
}

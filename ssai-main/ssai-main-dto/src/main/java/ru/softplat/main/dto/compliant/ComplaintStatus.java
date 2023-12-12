package ru.softplat.main.dto.compliant;

public enum ComplaintStatus {
    OPENED("Жалобу еще не взяли в работу"),
    REVIEW("Жалоба находится в процессе отработки"),
    CLOSED("Жалоба отработана и закрыта");

    public final String label;

    ComplaintStatus(String label) {
        this.label = label;
    }
}

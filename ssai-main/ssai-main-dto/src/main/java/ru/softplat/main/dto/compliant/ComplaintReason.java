package ru.softplat.main.dto.compliant;

public enum ComplaintReason {
    PIRATED_SOFTWARE("Пиратское ПО"),
    SELLER_FRAUD("Мошенничество со стороны продавца"),
    SOFTWARE_NOT_WORKING("ПО не работает");

    private final String reason;

    ComplaintReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}

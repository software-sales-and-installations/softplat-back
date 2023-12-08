package ru.softplat.main.dto.compliant;

public enum ComplaintReasonRequest {
    PIRATED_SOFTWARE("Пиратское ПО"),
    SELLER_FRAUD("Мошенничество со стороны продавца"),
    SOFTWARE_NOT_WORKING("ПО не работает");

    private final String reason;

    ComplaintReasonRequest(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}

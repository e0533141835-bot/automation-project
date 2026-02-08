package org.example.pages;

public enum BtlMenu {
    RIGHTS_REALIZATION("מיצוי זכויות"),
    BENEFITS("קצבאות והטבות"),
    INSURANCE("דמי ביטוח"),
    CONTACT_US("יצירת קשר");

    private final String hebrewName;

    BtlMenu(String hebrewName) {
        this.hebrewName = hebrewName;
    }

    public String getHebrewName() {
        return hebrewName;
    }
}
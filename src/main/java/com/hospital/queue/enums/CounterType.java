package com.hospital.queue.enums;

/**
 * Enum representing different service counters in the hospital.
 * Each counter maintains a separate queue.
 */
public enum CounterType {
    CONSULTATION("Consultation"),
    BILLING("Billing"),
    LAB("Laboratory"),
    PHARMACY("Pharmacy");

    private final String displayName;

    CounterType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

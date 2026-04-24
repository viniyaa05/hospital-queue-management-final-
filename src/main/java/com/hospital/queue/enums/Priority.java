package com.hospital.queue.enums;

/**
 * Enum representing patient priority level in queue.
 * Lower number = Higher priority.
 */
public enum Priority {
    EMERGENCY(1),   // Highest priority - immediate attention
    HIGH(2),        // Elderly patients (age >= 60), disabled
    NORMAL(3);      // Regular walk-in patients

    private final int level;

    Priority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

package com.hospital.queue.enums;

/**
 * Enum representing the status of a patient token in the queue.
 */
public enum TokenStatus {
    WAITING,    // Patient is waiting in queue
    SERVING,    // Patient is currently being served
    COMPLETED,  // Service completed
    SKIPPED     // Patient was skipped (absent/not responding)
}

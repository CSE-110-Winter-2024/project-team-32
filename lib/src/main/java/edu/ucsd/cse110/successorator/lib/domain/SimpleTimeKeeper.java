package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

/**
 * Simple implementation of time keeper that accesses current date and time
 */
public class SimpleTimeKeeper implements TimeKeeper {
    private LocalDateTime currentDateTime;

    /**
     * Grabs current date and time
     * @return current date and time
     */
    @Override
    public LocalDateTime getDateTime() {
        return currentDateTime;
    }

    /**
     * Sets current date and time
     * @param dateTime desired time to be set as current
     */
    @Override
    public void setDateTime(LocalDateTime dateTime) {
        this.currentDateTime = dateTime;
    }
}

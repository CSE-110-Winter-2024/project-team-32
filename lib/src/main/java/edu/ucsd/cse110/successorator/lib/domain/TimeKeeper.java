package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

/**
 * time keeper interface to access date and time
 */
public interface TimeKeeper {
    LocalDateTime getDateTime();
    void setDateTime(LocalDateTime dateTime);
}

package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

public interface TimeKeeper {
    LocalDateTime getDateTime();
    void setDateTime(LocalDateTime dateTime);
}

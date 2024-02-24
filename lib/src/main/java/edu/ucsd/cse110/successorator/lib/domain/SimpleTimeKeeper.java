package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

public class SimpleTimeKeeper implements TimeKeeper {
    private LocalDateTime currentDateTime;

    @Override
    public LocalDateTime getDateTime() {
        return currentDateTime;
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) {
        this.currentDateTime = dateTime;
    }
}

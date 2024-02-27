package edu.ucsd.cse110.successorator.lib.domain;


import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class RecurringMostImportantThing {
    public MostImportantThing mit;
    public String recurPeriod;
    private final @Nullable Integer id;

    public Integer id() {
        return this.id;
    }

    /**
     * Constructs the Recurring mit
     * @param mit Should be a fully populated MIT with the creation date being the first recurring
     *            date
      *
      *
      * @param recurPeriod String that must be "Daily", "Weekly", "Monthly", or "Yearly".
      *
      * @throws IllegalArgumentException if recurrence period is not Daily|Weekly|Monthly|Yearly
     */
    public RecurringMostImportantThing(MostImportantThing mit, String recurPeriod) {
        this.mit = mit;

        // maybe better way of doing this?
        String regex = "^(Daily|Weekly|Monthly|Yearly)$"; // regex to validate recurPeriod
        if (!recurPeriod.matches(regex)) {
            throw new IllegalArgumentException("Invalid Recurrence Period");
        }
        this.recurPeriod = recurPeriod;
        this.id = mit.id();

    }
    public boolean isRecurringDate(LocalDateTime date) {
        return false;
        // TODO - should do logic to work out if it is time for this mit to be generated
    }
    public MostImportantThing createMit(Long timeCreated) {
        return this.mit.withTimeCreated(timeCreated);
    }

}

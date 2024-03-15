package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.Nullable;

/**
 * Represents an MIT object that is pending/ that hasn't been assigned a date
 */
public class PendingMostImportantThing implements RepositoryObject {
    public MostImportantThing mit;
    private final @Nullable Integer id;

    public Integer id() {
        return this.id;
    }

    /**
     * Constructor to be fed a mit object with a placeholder time.
     * @param mit MIT with placeholder time, will be replaced so doesn't matter
     */
    public PendingMostImportantThing(MostImportantThing mit) {
        this.mit = mit;
        this.id = mit.id();
    }

    /**
     * Convert the current MIT to an MIT with a specific creation time
     * @param timeCreated the time in which an MIT is created
     * @return An MIT with the creation time timeCreated
     */
    public MostImportantThing convertToMit(Long timeCreated) {
        return this.mit.withTimeCreated(timeCreated);
    }

    /**
     * Convert the pending MIT to a Recurring MIT with specified creation time and recurring time
     * @param timeCreated When the MIT was created
     * @param recurPeriod When it recurs
     * @return a Recurring MIT that recurs on the specified period and created on curr date
     */
    public RecurringMostImportantThing convertToRecurring(Long timeCreated, String recurPeriod) {
        return new RecurringMostImportantThing(this.convertToMit(timeCreated), recurPeriod);
    }

    /**
     * Grabs current instance's MIT
     * @return current MIT
     */
    public MostImportantThing mit() {
        return this.mit;
    }
}

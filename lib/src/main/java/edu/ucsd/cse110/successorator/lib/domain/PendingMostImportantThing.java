package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.Nullable;

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
    public MostImportantThing convertToMit(Long timeCreated) {
        return this.mit.withTimeCreated(timeCreated);
    }

    public RecurringMostImportantThing convertToRecurring(Long timeCreated, String recurPeriod) {
        return new RecurringMostImportantThing(this.convertToMit(timeCreated), recurPeriod);
    }
    public MostImportantThing mit() {
        return this.mit;
    }
}

package edu.ucsd.cse110.successorator.lib.domain;

public class PendingMostImportantThing {
    MostImportantThing mit;


    /**
     * Constructor to be fed a mit object with a placeholder time.
     * @param mit MIT with placeholder time, will be replaced so doesn't matter
     */
    public PendingMostImportantThing(MostImportantThing mit) {
        this.mit = mit;
    }
    public MostImportantThing convertToMit(Long timeCreated) {
        return this.mit.withTimeCreated(timeCreated);
    }

    public RecurringMostImportantThing convertToRecurring(Long timeCreated, String recurPeriod) {
        return new RecurringMostImportantThing(this.convertToMit(timeCreated), recurPeriod);
    }
}

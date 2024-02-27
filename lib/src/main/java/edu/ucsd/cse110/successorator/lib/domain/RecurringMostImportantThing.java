package edu.ucsd.cse110.successorator.lib.domain;


import java.time.LocalDateTime;

public class RecurringMostImportantThing {
    MostImportantThing mit;
    String recurPeriod;
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
    }

    /**
     * Checks if, when given the current date and the date of the object, if it's time to be repeated
     *
     *
     * @param date LocalDateTime that represents TODO WHAT DOES THIS REPRESENT
     *
     * @returns boolean true if it's time to remake the Recurring MIT
     */
    public boolean isRecurringDate(LocalDateTime date) {
        LocalDateTime current = LocalDateTime.now();

        switch (recurPeriod) {
            case "Daily":

                if(current.getDayOfYear() != date.getDayOfYear()) {
                    return true;

                    //same day in different years
                } else if(current.getYear() != date.getYear()) {
                    return true;
                }

                break;
            case "Weekly":
                //identical day
                if(current.getYear() == date.getYear() && current.getDayOfYear() == date.getDayOfYear()) {
                    return false;

                } else if(current.getDayOfWeek() == current.getDayOfWeek()) {
                    return true;
                }
                break;

            case "Monthly":

                if(current.getYear() == date.getYear() && current.getDayOfYear() == date.getDayOfYear()) {
                    return false;
                } else if(current.getDayOfMonth() == current.getDayOfMonth()) {
                    return true;
                }

                break;
            case "Yearly":

                if(current.getYear() == date.getYear() && current.getDayOfYear() == date.getDayOfYear()) {
                    return false;
                } else if(current.getDayOfYear() == date.getDayOfYear()) {
                    return true;
                }

                break;
            default:
                //should already be protected, but still
                throw new IllegalArgumentException("Invalid Recurrence Period");
        }
        return false;
    }
    public MostImportantThing createMit(Long timeCreated) {
        return this.mit.withTimeCreated(timeCreated);
    }

}

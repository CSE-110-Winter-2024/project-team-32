package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Class which defines a MIT, stores the id, task, timeCreated, sortOrder,
 * and whether or not it has been completed
 */
public class MostImportantThing implements IMostImportantThing {
    private final @Nullable Integer id;
    private final @NonNull String task;
    private final @NonNull Long timeCreated;
    private @NonNull Boolean completed;
    private int sortOrder;

    private final String workContext;

    /**
     * Public constructor for MostImportantThing
     *
     * @param id          required id
     * @param task        what the task text should be
     * @param timeCreated epoch time, should come from java.lang.System.currentTimeMillis()
     * @param sortOrder   integer for sorting purposes
     * @param workContext what context the task should have
     */
    public MostImportantThing(@Nullable Integer id,
                              @NonNull String task,
                              @NonNull Long timeCreated,
                              int sortOrder,
                              boolean completed, String workContext)
    {
        this.id = id;
        this.task = task;
        this.timeCreated = timeCreated;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.workContext = workContext;
    }

    @Override
    public @Nullable Integer id() {
        return this.id;
    }
    @Override
    public @NonNull String task() {
        return this.task;
    }
    @Override
    public @NonNull Long timeCreated() {
        return this.timeCreated;
    }
    @Override
    public @NonNull Boolean completed() {
        return this.completed;
    }

    @NonNull
    public String workContext() {
        return this.workContext;
    }

    @Override
    public int sortOrder() {
        return this.sortOrder;
    }

    /**
     * returns a copy of itself with modified id
     * @param id new id
     * @return identical MostImportantThing but with changed id
     */
    @Override
    public MostImportantThing withId(int id) {
        return new MostImportantThing(id, this.task, this.timeCreated, this.sortOrder, this.completed, this.workContext);
    }

    /**
     * returns a copy of the MostImportantThing with the completed status
     * @param completed the new completed status
     * @return a new MostImportantThing with the new completed status set
     */
    @Override
    public MostImportantThing withCompleted(boolean completed) {
        return new  MostImportantThing(this.id,
                this.task,
                this.timeCreated,
                this.sortOrder,
                completed, "Home");
    }

    /**
     * returns a copy of itself with modified sort order
     * @param sortOrder new sort order
     * @return identical MostImportantThing but with changed sort order
     */
    @Override
    public MostImportantThing withSortOrder(int sortOrder) {
        return new MostImportantThing(this.id, this.task, this.timeCreated, sortOrder, this.completed, this.workContext);
    }
    public MostImportantThing withTimeCreated(long timeCreated) {
        return new MostImportantThing(this.id, this.task, timeCreated, this.sortOrder, this.completed, this.workContext);
    }

    /**
     * sets the completed value of mostImportantThing
     * @param b the completed value to be set
     */
    // TODO - this probably violates SRP
    @Override
    public void setCompleted(Boolean b) {
        this.completed = b;
    }

    /**
     * Custom equals method for two mostImportantThing objects
     * Equal if all sortOrder, id, task, timeCreated, and completed status
     * are the same
     * Compares current instance's object with parameter
     * @param o the mostImportantThing object to compare
     * @return true if they are equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MostImportantThing that = (MostImportantThing) o;
        return sortOrder == that.sortOrder && Objects.equals(id, that.id) && Objects.equals(task, that.task) && Objects.equals(timeCreated, that.timeCreated) && Objects.equals(completed, that.completed);
    }

    /**
     * Gets the hashCode value of the mostImportantThing based on it's id, task,
     * timeCreated, completed, and sortOrder values
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, task, timeCreated, completed, sortOrder);
    }
}

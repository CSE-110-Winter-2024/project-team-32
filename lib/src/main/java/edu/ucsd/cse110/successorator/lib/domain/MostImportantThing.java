package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class MostImportantThing {
    private final @Nullable Integer id;
    private final @NonNull String task;
    private final @NonNull Long timeCreated;
    private @NonNull Boolean completed;
    private int sortOrder;

    /**
     * Public constructor for MostImportantThing
     * @param id required id
     * @param task what the task text should be
     * @param timeCreated epoch time, should come from java.lang.System.currentTimeMillis()
     * @param sortOrder integer for sorting purposes
     */
    public MostImportantThing(@Nullable Integer id,
                              @NonNull String task,
                              @NonNull Long timeCreated,
                              int sortOrder,
                              boolean completed)
    {
        this.id = id;
        this.task = task;
        this.timeCreated = timeCreated;
        this.completed = completed;
        this.sortOrder = sortOrder;
    }

    public @Nullable Integer id() {
        return this.id;
    }
    public @NonNull String task() {
        return this.task;
    }
    public @NonNull Long timeCreated() {
        return this.timeCreated;
    }
    public @NonNull Boolean completed() {
        return this.completed;
    }
    public int sortOrder() {
        return this.sortOrder;
    }

    /**
     * returns a copy of itself with modified id
     * @param id new id
     * @return identical MostImportantThing but with changed id
     */

    public MostImportantThing withId(int id) {
        return new MostImportantThing(id, this.task, this.timeCreated, this.sortOrder, this.completed);
    }

    public MostImportantThing withCompleted(boolean completed) {
        MostImportantThing output = new  MostImportantThing(this.id, this.task, this.timeCreated, this.sortOrder, this.completed);
        output.setCompleted(completed);
        return output;
    }

    /**
     * returns a copy of itself with modified sort order
     * @param sortOrder new sort order
     * @return identical MostImportantThing but with changed sort order
     */
    public MostImportantThing withSortOrder(int sortOrder) {
        return new MostImportantThing(this.id, this.task, this.timeCreated, sortOrder, this.completed);
    }

    // TODO - this probably violates SRP
    public void setCompleted(Boolean b) {
        this.completed = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MostImportantThing that = (MostImportantThing) o;
        return sortOrder == that.sortOrder && Objects.equals(id, that.id) && Objects.equals(task, that.task) && Objects.equals(timeCreated, that.timeCreated) && Objects.equals(completed, that.completed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, timeCreated, completed, sortOrder);
    }
}

package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.MITContext;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.PendingMostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.RecurringMostImportantThing;

/**
 * Entity class of mostImportantThing in database
 */
@Entity(tableName = "most_important_things") // telling Room that this class is a database table
                                             // and I want it to be called "most_important_things
public class MostImportantThingEntity {
    @PrimaryKey(autoGenerate = true) // database IDs are generated for us
    @ColumnInfo(name = "id") // specify names of our fields so we can write queries against them
    public Integer id;

    @ColumnInfo(name = "task")
    public String task;

    @ColumnInfo(name = "timeCreated")
    public Long timeCreated;

    @ColumnInfo(name = "completed")
    public Boolean completed;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "work_context")
    public String workContext;

    @ColumnInfo(name = "is_recurring")
    public boolean isRecurring;
    @ColumnInfo(name = "is_pending")
    public boolean isPending;
    @ColumnInfo(name = "recur_period")
    public String recurPeriod;

    /**
     * Constructor for creating a mostImportantThingEntity, with a work context
     * of "Home"
     * @param task - the text this MIT should have
     * @param timeCreated - creation time in ms from epoch
     * @param sortOrder - the sort order int
     * @param completed whether or not the task is completed
     */
    MostImportantThingEntity(Integer id,
                             @NonNull String task,
                             @NonNull Long timeCreated,
                             int sortOrder,
                             boolean completed) {
        this.id = id;
        this.task = task;
        this.timeCreated = timeCreated;
        this.sortOrder = sortOrder;
        this.completed = completed;
        this.workContext = "Home";
        this.isRecurring = false;
        this.isPending = false;
        this.recurPeriod = "";
    }

    /**
     * Constructor for creating a mostImportantThingEntity, with a work context
     * of "Home"
     * @param task - the text this MIT should have
     * @param timeCreated - creation time in ms from epoch
     * @param sortOrder - the sort order int
     * @param completed whether or not the task is completed
     */
    @Ignore
    MostImportantThingEntity(Integer id,
                             @NonNull String task,
                             @NonNull Long timeCreated,
                             int sortOrder,
                             boolean completed,
                             boolean isPending,
                             boolean isRecurring,
                             String recurPeriod) {
        this.id = id;
        this.task = task;
        this.timeCreated = timeCreated;
        this.sortOrder = sortOrder;
        this.completed = completed;
        this.workContext = "Home";
        this.isRecurring = isRecurring;
        this.isPending = isPending;
        this.recurPeriod = recurPeriod;
    }

    /**
     * Constructor for creating a mostImportantThingEntity with specified work context
     * @param task - the text this MIT should have
     * @param timeCreated - creation time in ms from epoch
     * @param sortOrder - the sort order int
     * @param completed whether or not the task is completed
     * @param workContext the work context the entity should have
     */
    @Ignore
    MostImportantThingEntity(Integer id,
                             @NonNull String task,
                             @NonNull Long timeCreated,
                             int sortOrder,
                             boolean completed,
                             String workContext) {
        this.id = id;
        this.task = task;
        this.timeCreated = timeCreated;
        this.sortOrder = sortOrder;
        this.completed = completed;
        this.workContext = workContext;
        this.isRecurring = false;
        this.isPending = false;
        this.recurPeriod = "";
    }
    /**
     * Converts a mostImportantThing to an entity object
     * @param mostImportantThing the mostImportantThing to be converted
     * @return The converted mostImportantThing object
     */
    public static MostImportantThingEntity fromMostImportantThing(@NonNull MostImportantThing mostImportantThing) {
        var mit = new MostImportantThingEntity(mostImportantThing.id(),
                mostImportantThing.task(),
                mostImportantThing.timeCreated(),
                mostImportantThing.sortOrder(),
                mostImportantThing.completed(),
                mostImportantThing.workContext());

        mit.id = mostImportantThing.id();
        return mit;
    }
    /**
     * Converts a PendingMostImportantThing to an entity object
     * @param pendingMostImportantThing pMIT to be converted
     */
    public static MostImportantThingEntity fromMostImportantThing(@NonNull PendingMostImportantThing pendingMostImportantThing) {
        var pMit = new MostImportantThingEntity(pendingMostImportantThing.id(),
                pendingMostImportantThing.mit.task(),
                pendingMostImportantThing.mit.timeCreated(),
                pendingMostImportantThing.mit.sortOrder(),
                pendingMostImportantThing.mit.completed());
        pMit.isPending = true;
        pMit.id = pendingMostImportantThing.id();
        return pMit;
    }
    /**
     * Converts a RecurringMostImportantThing to an entity object
     * @param recurringMostImportantThing pMIT to be converted
     */
    public static MostImportantThingEntity fromMostImportantThing(@NonNull RecurringMostImportantThing recurringMostImportantThing) {
        var rMit = new MostImportantThingEntity(recurringMostImportantThing.id(),
                recurringMostImportantThing.mit.task(),
                recurringMostImportantThing.mit.timeCreated(),
                recurringMostImportantThing.mit.sortOrder(),
                recurringMostImportantThing.mit.completed());
        rMit.isRecurring = true;
        rMit.recurPeriod = recurringMostImportantThing.recurPeriod;
        rMit.id = recurringMostImportantThing.id();
        return rMit;
    }

    /**
     * Converts current iteration's MostImportantThingEntity object into a
     * MostImportantThing object
     * @return The converted MostImportantThing object
     */
    public @NonNull MostImportantThing toMostImportantThing() {
        return new MostImportantThing(this.id, this.task, this.timeCreated, this.sortOrder, this.completed, this.workContext);
    }

    /**
     * Converts current iteration's MostImportantThingEntity object into a
     * PendingMostImportantThing object
     * @return The converted PendingMostImportantThing object
     * @throws IllegalArgumentException if the entity isn't a pending object
     */
    public @NonNull PendingMostImportantThing toPendingMostImportantThing() {
        if (!this.isPending) {
            throw new IllegalArgumentException("Not a Pending Object");
        }
        var mit = new MostImportantThing(this.id, this.task, this.timeCreated, this.sortOrder, this.completed, this.workContext);
        return new PendingMostImportantThing(mit);
    }
    /**
     * Converts current iteration's MostImportantThingEntity object into a
     * RecurringMostImportantThing object
     * @return The converted RecurringMostImportantThing object
     * @throws IllegalArgumentException if the entity isn't a recurring object
     */
    public @NonNull RecurringMostImportantThing toRecurringMostImportantThing() {
        if (!this.isRecurring) {
            throw new IllegalArgumentException("Not a Pending Object");
        }
        var mit = new MostImportantThing(this.id, this.task, this.timeCreated, this.sortOrder, this.completed, this.workContext);
        return new RecurringMostImportantThing(mit, this.recurPeriod);
    }
}

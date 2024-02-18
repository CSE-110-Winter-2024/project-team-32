package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

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

    /**
     * Constructor for creating a mostImportantThingEntity
     * @param task - the text this MIT should have
     * @param timeCreated - creation time in ms from epoch
     * @param sortOrder - the sort order int
     */
    MostImportantThingEntity(Integer id, @NonNull String task, @NonNull Long timeCreated, int sortOrder, boolean completed) {
        this.id = id;
        this.task = task;
        this.timeCreated = timeCreated;
        this.sortOrder = sortOrder;
        this.completed = completed;
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
                                                mostImportantThing.completed());

        mit.id = mostImportantThing.id();
        return mit;
    }

    /**
     * Converts current iteration's MostImportantThingEntity object into a
     * MostImportantThing object
     * @return The converted MostImportantThing object
     */
    public @NonNull MostImportantThing toMostImportantThing() {
        return new MostImportantThing(this.id, this.task, this.timeCreated, this.sortOrder, this.completed);
    }
}

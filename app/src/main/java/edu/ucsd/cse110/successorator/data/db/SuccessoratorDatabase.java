package edu.ucsd.cse110.successorator.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database class for Successor using Room persistence
 */
@Database(entities = {MostImportantThingEntity.class}, version = 1)
public abstract class SuccessoratorDatabase extends RoomDatabase {
    /**
     * Provides access to DAOs for database operations
     * @return an instance of MostImportantThingDao
     */
    public abstract MostImportantThingDao mostImportantThingDao();
}

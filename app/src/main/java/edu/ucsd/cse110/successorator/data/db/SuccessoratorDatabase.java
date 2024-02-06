package edu.ucsd.cse110.successorator.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MostImportantThingEntity.class}, version = 1)
public abstract class SuccessoratorDatabase extends RoomDatabase {
    public abstract MostImportantThingDao MostImportantThingDao();
}

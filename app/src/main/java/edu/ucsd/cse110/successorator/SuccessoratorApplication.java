package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.RoomMostImportantThingRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThingRepository;

public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    public MostImportantThingRepository mostImportantThingRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        // init the database
        var database = Room.databaseBuilder(
                        getApplicationContext(),
                        SuccessoratorDatabase.class,
                        "successorator-database")
                .allowMainThreadQueries()
                .build();
        // feed that database into this.MITRepo
        this.mostImportantThingRepository = new RoomMostImportantThingRepository(database.mostImportantThingDao());

        // populate the database with some initial data on the first run.
        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        
        if (isFirstRun) { // by only checking if first run, we can trick it
            database.mostImportantThingDao().clear();
            this.mostImportantThingRepository.save(InMemoryDataSource.DEFAULT_MITS);
            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
            System.out.println("I have been first ran.");
        }
    }

    public MostImportantThingRepository getMostImportantThingRepository() {
        return this.mostImportantThingRepository;
    }
}

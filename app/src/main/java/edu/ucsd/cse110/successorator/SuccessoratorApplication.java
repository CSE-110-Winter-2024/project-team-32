package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.RoomMostImportantThingRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThingRepository;

/**
 * Application class for Successorator; Initializes database and manages interactions/data on the first run
 */
public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    public MostImportantThingRepository mostImportantThingRepository;

    /**
     * Initializes database and sets up the repository
     * Handles first run
     */
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

        //if first run, clear database and insert default data
        if (isFirstRun) { // by only checking if first run, we can trick it
            database.mostImportantThingDao().clear();
            this.mostImportantThingRepository.save(InMemoryDataSource.DEFAULT_MITS);
            //marks that first run has been ran
            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
            //log first run
            System.out.println("I have been first ran.");
        }
    }

    /**
     * Retrieves the instance's MITRepo
     * @return the MITRepo
     */
    public MostImportantThingRepository getMostImportantThingRepository() {
        return this.mostImportantThingRepository;
    }
}

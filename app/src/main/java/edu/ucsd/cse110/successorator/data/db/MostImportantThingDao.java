package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao // telling android that this is a DAO
public interface MostImportantThingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(MostImportantThingEntity mostImportantThing);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<MostImportantThingEntity> mostImportantThings);


    // defining selections
    @Query("SELECT * FROM most_important_things WHERE id = :id")
    MostImportantThingEntity find(int id);

    @Query("SELECT * FROM most_important_things ORDER BY sort_order")
    List<MostImportantThingEntity> findAll();

    @Query("SELECT * FROM most_important_things WHERE id=:id")
    LiveData<MostImportantThingEntity> findAsLiveData(int id);

    @Query("SELECT * FROM most_important_things ORDER BY sort_order")
    LiveData<List<MostImportantThingEntity>> findAllAsLiveData();

    // this will update when the corresponding database record does
    // this will be helpful!
    @Query("SELECT COUNT(*) FROM most_important_things")
    int count();

    // methods for minimum and maximum sort_order in database
    @Query("SELECT MIN(sort_order) FROM most_important_things")
    int getMinSortOrder();
    @Query("SELECT MAX(sort_order) FROM most_important_things")
    int getMaxSortOrder();

    // this behaves likes InMemoryDataSource.shiftSortOrders
    @Query("UPDATE most_important_things SET sort_order = sort_order + :by " +
            "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);

    // special append to keep ordering nice
    @Transaction // means the following code should all fail or or succeed together
    default int append(MostImportantThingEntity mostImportantThing) {
        var maxSortOrder = getMaxSortOrder();
        var newMostImportantThing = new MostImportantThingEntity(
                mostImportantThing.id,
                mostImportantThing.task, mostImportantThing.timeCreated, maxSortOrder + 1,
                mostImportantThing.completed
        );
        return Math.toIntExact(insert(newMostImportantThing));
    }

    @Transaction
    default int prepend(MostImportantThingEntity mostImportantThing) {
        shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
        var newMostImportantThing = new MostImportantThingEntity(
                mostImportantThing.id,
                mostImportantThing.task, mostImportantThing.timeCreated, getMinSortOrder() - 1,
                mostImportantThing.completed
        );
        return Math.toIntExact(insert(newMostImportantThing));
    }

    @Query("DELETE FROM most_important_things WHERE id = :id")
    void delete(int id);

    @Query("UPDATE most_important_things set completed = NOT completed " +
            "WHERE id = :id")
    void toggleCompleted(int id);

    @Query("DELETE FROM most_important_things")
    void clear();
}

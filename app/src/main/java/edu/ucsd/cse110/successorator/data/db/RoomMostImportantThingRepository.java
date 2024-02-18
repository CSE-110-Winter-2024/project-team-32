package edu.ucsd.cse110.successorator.data.db;

import android.sax.Element;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThingRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

/**
 * Implementation of MostImportantThingRepository interface using Room
 */
public class RoomMostImportantThingRepository implements MostImportantThingRepository {
    private final MostImportantThingDao mostImportantThingDao;

    /**
     * Constructor for RoomMostImportantThingRepository
     * @param mostImportantThingDao The DAO for MostImportantThings
     */
    public RoomMostImportantThingRepository(MostImportantThingDao mostImportantThingDao) {
        this.mostImportantThingDao = mostImportantThingDao;
    }

    /**
     * finds a MostImportantThing by it's id
     * @param id the ID of the MostImportantThing you are trying to find
     * @return The subject with the MostImportantThing found
     */

    @Override
    public Subject<MostImportantThing> find(int id) {
        LiveData<MostImportantThingEntity> entityLiveData = mostImportantThingDao.findAsLiveData(id);
        // this map takes a LiveData<X> and turns it into a LiveData<Y>
        LiveData<MostImportantThing> mostImportantThingLiveData = Transformations.map(entityLiveData, MostImportantThingEntity::toMostImportantThing);
        return new LiveDataSubjectAdapter<>(mostImportantThingLiveData);
    }

    /**
     * Finds all MostImportantThings
     * @return A Subject List of all the MostImportantThings
     */
    @Override
    public Subject<List<MostImportantThing>> findAll() {
        var entitiesLiveData = mostImportantThingDao.findAllAsLiveData();
        var mostImportantThingsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(MostImportantThingEntity::toMostImportantThing)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(mostImportantThingsLiveData);
    }

    /**
     * Saves a MostImportantThing
     * @param mostImportantThing the MostImportantThing to be saved
     */
    @Override
    public void save(MostImportantThing mostImportantThing) {
        this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(mostImportantThing));
    }

    /**
     * Saves a list of MostImportantThings
     * @param mostImportantThings The List of MostImportantThings to save
     */
    @Override
    public void save(List<MostImportantThing> mostImportantThings) {
        var entities = mostImportantThings.stream()
                .map(MostImportantThingEntity::fromMostImportantThing)
                .collect(Collectors.toList());
        this.mostImportantThingDao.insert(entities);
    }

    /**
     * Prepends a mostImportantThing
     * @param mostImportantThing The mostImportantThing to prepend
     */
    @Override
    public void prepend(MostImportantThing mostImportantThing) {
        mostImportantThingDao.prepend(MostImportantThingEntity.fromMostImportantThing(mostImportantThing));
    }

    /**
     * Appends a mostImportantThing
     * @param mostImportantThing The mostImportantThing to append
     */
    @Override
    public void append(MostImportantThing mostImportantThing) {
        mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mostImportantThing));
    }

    /**
     * Removes a mostImportantThing by it's id
     * @param id The ID of the mostImportantThing to remove
     */
    @Override
    public void remove(int id) {
        mostImportantThingDao.delete(id);
    }

    /**
     * Clears all mostImportantThings from repository
     */
    public void clear() {
        this.mostImportantThingDao.clear();
    }

    /**
     * Toggles the completed status of the mostImportantThing with the id
     * @param id The ID of the mostImportantThing to mark completed
     */
    public void toggleCompleted(int id) {
        System.out.println("Toggling completed");
        if (this.mostImportantThingDao.find(id).completed) {
            //Move the item to the aboslute top of the list
            this.moveToTop(id);
        }
        else {
            //If te item was not done, move it to the top of the finished
            //portion of the list (This is US8 that was already implemented
            //During the implementation of US4)
            this.moveToTopOfFinished(id);
        }
        this.mostImportantThingDao.toggleCompleted(id);
    }

    /**
     * Moves a mostImportantThing to the top of the unfinished list
     * @param id The ID of the mostImportantThing to move
     */
    public void moveToTop(int id) {
        this.mostImportantThingDao.prepend(this.mostImportantThingDao.find(id));
    }

    /**
     * Moves a mostImportantThing to the top of the finished list
     * @param id The ID of the mostImportantThing to move
     */
    public void moveToTopOfFinished(int id) {
        var ElementList = this.mostImportantThingDao.findAll();
        int numElems = ElementList.size();
        int insertIdx = 0;
        //Find the Index of the first element where the next element is completed
        for (int i = 0; i < numElems; i++) {
            if (ElementList.get(i).completed && (ElementList.get(i).id != id)) {
                break;
            }
            insertIdx++;
        }
        //If there are no unfinished tasks that aren't the one we're moving
        if (insertIdx == 0) {
            /* We do not need to need to do any reorganizing, as this unfinished
            task must already be at the top of the list, which is the desired
            position */
        }
        //If there are only unfinished tasks
        else if (insertIdx == numElems) {
            this.mostImportantThingDao.append(this.mostImportantThingDao.find(id));
        }
        //There are both finished and unfinished tasks
        else {
            int sortOrder = ElementList.get(insertIdx).toMostImportantThing().sortOrder();
            this.mostImportantThingDao.shiftSortOrders(ElementList.get(insertIdx).toMostImportantThing().sortOrder(), this.mostImportantThingDao.getMaxSortOrder(), 1);
            this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(this.mostImportantThingDao.find(id).toMostImportantThing().withSortOrder(sortOrder)));
        }
    }

    /**
     * Adds a new MostImportantThing to the repository
     * @param mit The MostImportantThing being added
     */
    public void addNewMostImportantThing(MostImportantThing mit) {
        var ElementList = this.mostImportantThingDao.findAll();
        int numElems = ElementList.size();
        int insertIdx = 0;
        //Find index of the first element where the next element is completed
        for (int i = 0; i < numElems; i++) {
            if (ElementList.get(i).completed) {
                break;
            }
            insertIdx++;
        }
        //If there are no elements in the list
        if (numElems == 0) {
            this.mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mit));
        }
        //If there are only completed MITs in the list
        else if (insertIdx == 0) {
            //Add to the front of the list
            this.mostImportantThingDao.shiftSortOrders(this.mostImportantThingDao.getMinSortOrder(), this.mostImportantThingDao.getMaxSortOrder(), 1);
            this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(mit.withSortOrder(this.mostImportantThingDao.getMinSortOrder() - 1)));
        }
        //If there are only uncompleted MITs in the list
        else if (insertIdx == numElems) {
            //Add to the bottom of the list
            this.mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mit));
        }
        //There are uncompleted and completed MITs
        else {
            //Shift all completed MITs down, insert new one before them
            int sortOrder = ElementList.get(insertIdx).toMostImportantThing().sortOrder();
            this.mostImportantThingDao.shiftSortOrders(ElementList.get(insertIdx).toMostImportantThing().sortOrder(), this.mostImportantThingDao.getMaxSortOrder(), 1);
            this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(mit.withSortOrder(sortOrder)));

        }
    }

    /**
     * Count of the number of things in the repository
     * @return The count of the mostImportantThings
     */
    public int count() {
        return this.mostImportantThingDao.count();
    }
}

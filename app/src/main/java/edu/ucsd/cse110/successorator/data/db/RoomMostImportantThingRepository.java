package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThingRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;
public class RoomMostImportantThingRepository implements MostImportantThingRepository {
    private final MostImportantThingDao mostImportantThingDao;

    public RoomMostImportantThingRepository(MostImportantThingDao mostImportantThingDao) {
        this.mostImportantThingDao = mostImportantThingDao;
    }

//    @Override
    public Subject<MostImportantThing> find(int id) {
        LiveData<MostImportantThingEntity> entityLiveData = mostImportantThingDao.findAsLiveData(id);
        // this map takes a LiveData<X> and turns it into a LiveData<Y>
        LiveData<MostImportantThing> mostImportantThingLiveData = Transformations.map(entityLiveData, MostImportantThingEntity::toMostImportantThing);
        return new LiveDataSubjectAdapter<>(mostImportantThingLiveData);
    }

//    @Override
    public Subject<List<MostImportantThing>> findAll() {
        var entitiesLiveData = mostImportantThingDao.findAllAsLiveData();
        var mostImportantThingsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(MostImportantThingEntity::toMostImportantThing)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(mostImportantThingsLiveData);
    }

//    @Override
    public void save(MostImportantThing mostImportantThing) {
        this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(mostImportantThing));
    }

//    @Override
    public void save(List<MostImportantThing> mostImportantThings) {
        var entities = mostImportantThings.stream()
                .map(MostImportantThingEntity::fromMostImportantThing)
                .collect(Collectors.toList());
        this.mostImportantThingDao.insert(entities);
    }

//    @Override
    public void prepend(MostImportantThing mostImportantThing) {
        mostImportantThingDao.prepend(MostImportantThingEntity.fromMostImportantThing(mostImportantThing));
    }

//    @Override
    public void append(MostImportantThing mostImportantThing) {
        mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mostImportantThing));
    }

//    @Override
    public void remove(int id) {
        mostImportantThingDao.delete(id);
    }

    public void clear() {
        this.mostImportantThingDao.clear();
    }

    public void toggleCompleted(int id) {
        System.out.println("Toggling completed");
        if (this.mostImportantThingDao.find(id).completed) {
            this.moveToTop(id);
        }
        else {
            this.moveToTopOfFinished(id);
        }
        this.mostImportantThingDao.toggleCompleted(id);
    }

    public void moveToTop(int id) {
        this.mostImportantThingDao.prepend(this.mostImportantThingDao.find(id));
    }

    public void moveToTopOfFinished(int id) {
        var ElementList = this.mostImportantThingDao.findAll();
        int numElems = ElementList.size();
        int insertIdx = 0;
        for (int i = 0; i < numElems; i++) {
            if (ElementList.get(i).completed && (ElementList.get(i).id != id)) {
                break;
            }
            insertIdx++;
        }
        System.out.println("InsertIdx is " + insertIdx);
        if (insertIdx == 0) {
            this.mostImportantThingDao.shiftSortOrders(this.mostImportantThingDao.getMinSortOrder(), this.mostImportantThingDao.getMaxSortOrder(), 1);

        }
        else if (insertIdx == numElems) {
            this.mostImportantThingDao.append(this.mostImportantThingDao.find(id));
        }
        else {
            int sortOrder = ElementList.get(insertIdx).toMostImportantThing().sortOrder();
            this.mostImportantThingDao.shiftSortOrders(ElementList.get(insertIdx).toMostImportantThing().sortOrder(), this.mostImportantThingDao.getMaxSortOrder(), 1);
            this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(this.mostImportantThingDao.find(id).toMostImportantThing().withSortOrder(sortOrder)));

        }
    }

    public void addNewMostImportantThing(MostImportantThing mit) {
        var ElementList = this.mostImportantThingDao.findAll();
        int numElems = ElementList.size();
        int insertIdx = 0;
        for (int i = 0; i < numElems; i++) {
            if (ElementList.get(i).completed) {
                break;
            }
            insertIdx++;
        }
        System.out.println("InsertIdx is " + insertIdx);
        if (numElems == 0) {
            this.mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mit));
        }
        else if (insertIdx == 0) {
            this.mostImportantThingDao.shiftSortOrders(this.mostImportantThingDao.getMinSortOrder(), this.mostImportantThingDao.getMaxSortOrder(), 1);
            this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(mit.withSortOrder(this.mostImportantThingDao.getMinSortOrder() - 1)));
        }
        else if (insertIdx == numElems) {
            this.mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mit));
        }
        else {
            int sortOrder = ElementList.get(insertIdx).toMostImportantThing().sortOrder();
            this.mostImportantThingDao.shiftSortOrders(ElementList.get(insertIdx).toMostImportantThing().sortOrder(), this.mostImportantThingDao.getMaxSortOrder(), 1);
            this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(mit.withSortOrder(sortOrder)));

        }
    }
    public int count() {
        return this.mostImportantThingDao.count();
    }

    private long getReferenceTimeForRemoval(LocalDateTime time) {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, 2);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        // Adjust to yesterday's 2 a.m. if current time is before 2 a.m.
//        if (System.currentTimeMillis() < cal.getTimeInMillis()) {
//            cal.add(Calendar.DATE, -1);
//        }
//        return cal.getTimeInMillis();
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    // Method to filter tasks that should be removed
    private List<Integer> filterTasksForRemoval(List<MostImportantThingEntity> elements, long cutoffTime) {
        List<Integer> tasksToRemove = new ArrayList<>();
        for (MostImportantThingEntity element : elements) {
            if (element.completed && element.toMostImportantThing().timeCreated() < cutoffTime) {
                tasksToRemove.add(element.id);
            }
        }
        return tasksToRemove;
    }

    // Focused task removal method
    public void removeCompletedTasks() {
        long cutoffTime = getReferenceTimeForRemoval(LocalDateTime.now());
        List<MostImportantThingEntity> elements = mostImportantThingDao.findAll();
        List<Integer> tasksToRemove = filterTasksForRemoval(elements, cutoffTime);

        for (Integer taskId : tasksToRemove) {
            this.remove(taskId);
        }
    }
    public void removeCompletedTasks(LocalDateTime time) {
        long cutoffTime = getReferenceTimeForRemoval(time);
        List<MostImportantThingEntity> elements = mostImportantThingDao.findAll();
        List<Integer> tasksToRemove = filterTasksForRemoval(elements, cutoffTime);

        for (Integer taskId : tasksToRemove) {
            this.remove(taskId);
        }
    }
}

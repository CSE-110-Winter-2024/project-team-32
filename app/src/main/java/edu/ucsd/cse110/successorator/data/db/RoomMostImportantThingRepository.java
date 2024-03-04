package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.Transformations;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThingRepository;
import edu.ucsd.cse110.successorator.lib.domain.PendingMostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.RecurringMostImportantThing;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

/**
 * Implementation of MostImportantThingRepository interface using Room
 */
public class RoomMostImportantThingRepository implements MostImportantThingRepository {
    private final MostImportantThingDao mostImportantThingDao;
    // TODO - THIS IS OLD, NEED TO MAKE WORK WITH THE NEW UPDATED DAO
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
        var entityLiveData = mostImportantThingDao.findAsLiveData(id); //returning null!!
        // this map takes a LiveData<X> and turns it into a LiveData<Y>

        var mostImportantThingLiveData = Transformations.map(entityLiveData, MostImportantThingEntity::toMostImportantThing);
        return new LiveDataSubjectAdapter<>(mostImportantThingLiveData);
    }


    /**
     * Finds all MostImportantThings that are not pending or recurring
     * @return A Subject List of all the MostImportantThings
     */
    @Override
    public Subject<List<MostImportantThing>> findAllNormal() {
        var entitiesLiveData = mostImportantThingDao.findAllNormalAsLiveData();
        var mostImportantThingsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(MostImportantThingEntity::toMostImportantThing)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(mostImportantThingsLiveData);
    }

    /**
     * Finds all PendingMostImportantThings
     * @return A Subject List of all the MostImportantThings
     */
    @Override
    public Subject<List<PendingMostImportantThing>> findAllPending() {
        var entitiesLiveData = mostImportantThingDao.findAllPendingAsLiveData();
        var mostImportantThingsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(MostImportantThingEntity::toPendingMostImportantThing)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(mostImportantThingsLiveData);
    }

    /**
     * Finds all RecurringMostImportantThings
     * @return A Subject List of all the MostImportantThings
     */
    @Override
    public Subject<List<RecurringMostImportantThing>> findAllRecurring() {
        var entitiesLiveData = mostImportantThingDao.findAllRecurringAsLiveData();
        var mostImportantThingsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(MostImportantThingEntity::toRecurringMostImportantThing)
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
     * Prepends a pendingMostImportantThing
     * @param pendingMostImportantThing The pendingMostImportantThing to append
     */
    @Override
    public void prepend(PendingMostImportantThing pendingMostImportantThing) {
        mostImportantThingDao.prepend(MostImportantThingEntity.fromMostImportantThing(pendingMostImportantThing));
    }

    /**
     * Prepends a recurringMostImportantThing
     * @param recurringMostImportantThing The recurringMostImportantThing to append
     */
    @Override
    public void prepend(RecurringMostImportantThing recurringMostImportantThing) {
        mostImportantThingDao.prepend(MostImportantThingEntity.fromMostImportantThing(recurringMostImportantThing));
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
     * Appends a pendingMostImportantThing
     * @param pendingMostImportantThing The pendingMostImportantThing to append
     */
    @Override
    public void append(PendingMostImportantThing pendingMostImportantThing) {
        mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(pendingMostImportantThing));
    }

    /**
     * Appends a recurringMostImportantThing
     * @param recurringMostImportantThing The recurringMostImportantThing to append
     */
    @Override
    public void append(RecurringMostImportantThing recurringMostImportantThing) {
        mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(recurringMostImportantThing));
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
        var ElementList = this.mostImportantThingDao.findAllMits();
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
        var ElementList = this.mostImportantThingDao.findAllMits();
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
     * Adds a new MostImportantThing to the repository
     * @param mit The MostImportantThing being added
     */
    public void addNewRecurringMostImportantThing(RecurringMostImportantThing mit) {
        this.mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mit));
        //**** For now just adding the new recurringMIT to the end ********
    }

    /**
     * Adds a recurring MIT to tomorrow's view if tomorrow's date is the scheduled date
     * Will not add to tomorrow's view if already present
     */
    public void updateRecurringMits() {





        /*var recurringMITs = mostImportantThingDao.findAllRecurrings();

        for (var recurringMIT : recurringMITs) {
            switch (recurringMIT.recurPeriod) {
                case "Daily":
                    //Add new MIT for today and tomorrow
                    addNewMostImportantThing(recurringMIT.toRecurringMostImportantThing().mit);
                    break;
                case "Weekly":
                    //If tomorrow lines up with the day of week created, add to tomorrow
                    RecurringMostImportantThing recurring = recurringMIT.toRecurringMostImportantThing();
                    Date recurringDate = new Date(recurring.mit.timeCreated());
                    if (containsNormalMITInTomorrow(recurring.mit)) {

                    }
                    break;
                case "Monthly":
                    //If tomorrow lines up with the day of month created, add to tomorrow
                    break;
                case "Yearly":
                    //If tomorrow lines up with the day of year created, add to tomorrow
                    break;
                default:
                    break;
            }
        }*/

        var tomorrowMITs = mostImportantThingDao.findAllMits();
        var recurringMITs = mostImportantThingDao.findAllRecurrings();

        Date dateCreatedMinusOneDay;
        Date currDate = new Date();

        //going through all the today/tomorrow mits and removing the today mits
        //now tomorrowMITs actually contains tomorrow MITs
        List<MostImportantThingEntity> tomorrowMITsList = new ArrayList<>();
        for (var tomorrowMIT : tomorrowMITs) {
            dateCreatedMinusOneDay = new Date(tomorrowMIT.timeCreated - TimeUnit.DAYS.toMillis(1));
            Instant instant1 = dateCreatedMinusOneDay.toInstant()
                    .truncatedTo(ChronoUnit.DAYS);
            Instant instant2 = currDate.toInstant()
                    .truncatedTo(ChronoUnit.DAYS);
            if (!instant1.equals(instant2)) {
                continue;
            }
            tomorrowMITsList.add(tomorrowMIT);
        }

        //need to check the scheduled time for the recurring mit
        // remove the ones that aren't tomorrow
        Date tomorrowDate = new Date(currDate.getTime() + 86400000); //number of milliseconds in a day
        List<MostImportantThingEntity> recurringMITsList = new ArrayList<MostImportantThingEntity>();
        for (var recurringMIT : recurringMITs) {
            //System.out.println("test looking at: " + recurringMIT);
            RecurringMostImportantThing recurring = recurringMIT.toRecurringMostImportantThing();
            Date recurringDate = new Date(recurring.mit.timeCreated());
            if (recurringDate.equals(tomorrowDate) || recurringDate.before(tomorrowDate)) {
                recurringMITsList.add(recurringMIT);
                //updates the timeCreated to the next week/month/year in advance, so next time when checking
                //it will remind at the correct time
                if (recurringMIT.recurPeriod.equals("Daily")) {
                    recurring.mit = recurring.mit.withTimeCreated(recurring.mit.timeCreated() + 86400000);
                }
                else if (recurringMIT.recurPeriod.equals("Weekly")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(recurring.mit.timeCreated());
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                    recurring.mit = recurring.mit.withTimeCreated(calendar.getTimeInMillis());
                }
                else if (recurringMIT.recurPeriod.equals("Monthly")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(recurring.mit.timeCreated());
                    calendar.add(Calendar.MONTH, 1);
                    recurring.mit = recurring.mit.withTimeCreated(calendar.getTimeInMillis());
                }
                else if (recurringMIT.recurPeriod.equals("Yearly")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(recurring.mit.timeCreated());
                    calendar.add(Calendar.YEAR, 1);
                    recurring.mit = recurring.mit.withTimeCreated(calendar.getTimeInMillis());
                }
                //System.out.println("test print list: " + recurringMIT);
            }
        }

        //checking if the recurring mit is already a tomorrow mit
        boolean isTomorrow = false;
        for (var recurringMIT : recurringMITsList) {
            isTomorrow = false;
            for (var tomorrowMIT : tomorrowMITsList) {
                //if recurring MIT is already a tomorrow MIT
                if (recurringMIT == tomorrowMIT) {
                    isTomorrow = true;
                    break;
                }
            }
            //if the recurring mit hasn't been added yet, add it to front
            if (isTomorrow == false) {
                MostImportantThing mit = new MostImportantThing(
                        null,
                        recurringMIT.toRecurringMostImportantThing().mit.task(),
                        recurringMIT.toRecurringMostImportantThing().mit.timeCreated(),
                        recurringMIT.toRecurringMostImportantThing().mit.sortOrder(),
                        recurringMIT.toRecurringMostImportantThing().mit.completed(),
                        recurringMIT.toRecurringMostImportantThing().mit.workContext());
                addNewMostImportantThing(mit);
                //tomorrowMITsList.add(mit);;
                //addNewRecurringMostImportantThing(recurringMIT.toRecurringMostImportantThing());
                //System.out.println("mit id is: " + mit);
                //System.out.println("recurring id is: " + recurringMIT.toRecurringMostImportantThing());
            }
        }
    }

    /**
     * Count of the number of things in the repository
     * @return The count of the mostImportantThings
     */
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
        LocalDateTime referenceTime = time.withHour(2).withMinute(0).withSecond(0).withNano(0);

        // Adjust to yesterday's 2 a.m. if current time is before today's 2 a.m.
        if (time.toLocalTime().isBefore(LocalTime.of(2, 0))) {
            referenceTime = referenceTime.minusDays(1);
        }
        return referenceTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    // Method to filter tasks that should be removed
    private List<Integer> filterTasksForRemoval(List<MostImportantThingEntity> elements, long cutoffTime) {
        List<Integer> tasksToRemove = new ArrayList<>();
        for (MostImportantThingEntity element : elements) {
            System.out.println("Task: " + element.task);
            System.out.println("Time Created: " + element.toMostImportantThing().timeCreated());
            System.out.println("cutoffTime: " + cutoffTime);
            if (element.completed && element.toMostImportantThing().timeCreated() < cutoffTime) {
                tasksToRemove.add(element.id);
            }
        }
        return tasksToRemove;
    }

    // Focused task removal method
    public void removeCompletedTasks() {
        long cutoffTime = getReferenceTimeForRemoval(LocalDateTime.now());
        List<MostImportantThingEntity> elements = mostImportantThingDao.findAllMits();
        List<Integer> tasksToRemove = filterTasksForRemoval(elements, cutoffTime);

        for (Integer taskId : tasksToRemove) {
            this.remove(taskId);
        }
    }
    public void removeCompletedTasks(LocalDateTime time) {
        long cutoffTime = getReferenceTimeForRemoval(time);
        var elements = mostImportantThingDao.findAllMits();
        System.out.println("Dao: " + mostImportantThingDao);
        System.out.println("this many found: " + elements.size());
        List<Integer> tasksToRemove = filterTasksForRemoval(elements, cutoffTime);

        for (Integer taskId : tasksToRemove) {
            this.remove(taskId);
        }
    }

    private boolean sameDayOfWeek(Date dateOne, Date dateTwo) {
        Calendar calOne = Calendar.getInstance();
        calOne.setTime(dateOne);
        Calendar calTwo = Calendar.getInstance();
        calTwo.setTime(dateTwo);
        return (calOne.get(Calendar.DAY_OF_WEEK) == calTwo.get(Calendar.DAY_OF_WEEK));
    }

    private boolean sameDayOfMonth(Date dateOne, Date dateTwo) {
        Calendar calOne = Calendar.getInstance();
        calOne.setTime(dateOne);
        Calendar calTwo = Calendar.getInstance();
        calTwo.setTime(dateTwo);
        return (calOne.get(Calendar.DAY_OF_MONTH) == calTwo.get(Calendar.DAY_OF_MONTH));
    }

    private boolean sameDayOfYear(Date dateOne, Date dateTwo) {
        Calendar calOne = Calendar.getInstance();
        calOne.setTime(dateOne);
        Calendar calTwo = Calendar.getInstance();
        calTwo.setTime(dateTwo);
        return (calOne.get(Calendar.DAY_OF_YEAR) == calTwo.get(Calendar.DAY_OF_YEAR));
    }

    private boolean containsNormalMIT(MostImportantThingEntity mitEntity) {
        var allMitEntities = this.mostImportantThingDao.findAll();
        for (var entity : allMitEntities) {
            if (!entity.isPending
                    && !entity.isRecurring
                    && (entity.workContext.equals(mitEntity.workContext))
                    && (entity.task.equals(mitEntity.task))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNormalMITInTomorrow(MostImportantThingEntity mitEntity) {
        var allMitEntities = this.mostImportantThingDao.findAll();
        for (var entity : allMitEntities) {
            if (!entity.isPending
                    && !entity.isRecurring
                    && (entity.workContext.equals(mitEntity.workContext))
                    && (entity.task.equals(mitEntity.task))) {
                //Check date is tomorrow
                Date date = new Date();
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.setTime(date);
                tomorrow.add(Calendar.DATE, 1);

                Calendar entityTime = Calendar.getInstance();
                entityTime.setTimeInMillis(entity.timeCreated);
                if (entityTime.before(tomorrow)) {
                    return true;
                }
            }
        }
        return false;
    }

}

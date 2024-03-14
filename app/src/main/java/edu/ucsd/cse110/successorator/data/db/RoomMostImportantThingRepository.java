package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.Transformations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.ContextOrderer;
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
    private Date currDate;
    // TODO - THIS IS OLD, NEED TO MAKE WORK WITH THE NEW UPDATED DAO

    /**
     * Constructor for RoomMostImportantThingRepository
     *
     * @param mostImportantThingDao The DAO for MostImportantThings
     */
    public RoomMostImportantThingRepository(MostImportantThingDao mostImportantThingDao, Date currDate) {
        this.mostImportantThingDao = mostImportantThingDao;
        this.currDate = currDate;
    }

    /**
     * finds a MostImportantThing by it's id
     *
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
     *
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
     *
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
     * Finds all PendingMostImportantThings of a particular context
     *
     * @return A Subject List of all the MostImportantThings
     */
    @Override
    public Subject<List<PendingMostImportantThing>> findAllPending(String context) {
        var entitiesLiveData = mostImportantThingDao.findAllPendingAsLiveData(context);
        var mostImportantThingsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(MostImportantThingEntity::toPendingMostImportantThing)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(mostImportantThingsLiveData);
    }

    /**
     * Finds all RecurringMostImportantThings
     *
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
     * Finds all MostImportantThings of a given Context
     * @return A Subject List of all the MostImportantThings
     */
    @Override
    public Subject<List<MostImportantThing>> findAllOfContext(String context) {
        var entitiesLiveData = mostImportantThingDao.findAllOfContextAsLiveData(context);
        var mostImportantThingsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(MostImportantThingEntity::toMostImportantThing)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(mostImportantThingsLiveData);
    }

    /**
     * Finds all RecurringMostImportantThings of a particular context
     * @return A Subject List of all the MostImportantThings
     */
    @Override
    public Subject<List<RecurringMostImportantThing>> findAllRecurring(String context) {
        var entitiesLiveData = mostImportantThingDao.findAllRecurringAsLiveData(context);
        var mostImportantThingsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(MostImportantThingEntity::toRecurringMostImportantThing)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(mostImportantThingsLiveData);
    }

    /**
     * Saves a MostImportantThing
     *
     * @param mostImportantThing the MostImportantThing to be saved
     */
    @Override
    public void save(MostImportantThing mostImportantThing) {
        this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(mostImportantThing));
    }

    /**
     * Saves a list of MostImportantThings
     *
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
     *
     * @param mostImportantThing The mostImportantThing to prepend
     */
    @Override
    public void prepend(MostImportantThing mostImportantThing) {
        mostImportantThingDao.prepend(MostImportantThingEntity.fromMostImportantThing(mostImportantThing));
    }

    /**
     * Prepends a pendingMostImportantThing
     *
     * @param pendingMostImportantThing The pendingMostImportantThing to append
     */
    @Override
    public void prepend(PendingMostImportantThing pendingMostImportantThing) {
        mostImportantThingDao.prepend(MostImportantThingEntity.fromMostImportantThing(pendingMostImportantThing));
    }

    /**
     * Prepends a recurringMostImportantThing
     *
     * @param recurringMostImportantThing The recurringMostImportantThing to append
     */
    @Override
    public void prepend(RecurringMostImportantThing recurringMostImportantThing) {
        mostImportantThingDao.prepend(MostImportantThingEntity.fromMostImportantThing(recurringMostImportantThing));
    }

    /**
     * Appends a mostImportantThing
     *
     * @param mostImportantThing The mostImportantThing to append
     */
    @Override
    public void append(MostImportantThing mostImportantThing) {
        mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mostImportantThing));
    }

    /**
     * Appends a pendingMostImportantThing
     *
     * @param pendingMostImportantThing The pendingMostImportantThing to append
     */
    @Override
    public void append(PendingMostImportantThing pendingMostImportantThing) {
        mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(pendingMostImportantThing));
    }

    /**
     * Appends a recurringMostImportantThing
     *
     * @param recurringMostImportantThing The recurringMostImportantThing to append
     */
    @Override
    public void append(RecurringMostImportantThing recurringMostImportantThing) {
        mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(recurringMostImportantThing));
    }

    /**
     * Removes a mostImportantThing by it's id
     *
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
     *
     * @param id The ID of the mostImportantThing to mark completed
     */
    public void toggleCompleted(int id) {
        System.out.println("Toggling completed");
        if (this.mostImportantThingDao.find(id).completed) {
            //Move the item to the aboslute top of the list

            //this.moveToTop(id);

            this.moveToTopOfContext(id);
        } else {
            //If te item was not done, move it to the top of the finished
            //portion of the list (This is US8 that was already implemented
            //During the implementation of US4)
            this.moveToTopOfFinished(id);
        }
        this.mostImportantThingDao.toggleCompleted(id);
    }

    /**
     * Moves a MIT to the bottom of it's given context
     *
     * @param id the ID of the MIT to move
     */
    public void moveToBottomOfContext(int id) {
        addNewMostImportantThing(this.mostImportantThingDao.find(id).toMostImportantThing());
    }


    /**
     * Moves and MIT to the top of it's given context
     *
     * @param id the ID of the MIT that we're movings
     */
    public void moveToTopOfContext(int id) {
        var ElementList = this.mostImportantThingDao.findAllMits();
        int numElems = ElementList.size();
        ContextOrderer con = new ContextOrderer();
        int insertIdx = 0;

        for(int i = 0; i < numElems; i ++) {
            if(ElementList.get(i).toMostImportantThing().completed() ||
                    con.compare(this.mostImportantThingDao.find(id).workContext, ElementList.get(i).workContext) <= 0 ) {
                break;
            }
            insertIdx ++;
        }


        int sortOrder = ElementList.get(insertIdx).toMostImportantThing().sortOrder();
        this.mostImportantThingDao.shiftSortOrders(ElementList.get(insertIdx).toMostImportantThing().sortOrder(), this.mostImportantThingDao.getMaxSortOrder(), 1);
        this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(this.mostImportantThingDao.find(id).toMostImportantThing().withSortOrder(sortOrder)));


    }

    /**
     * Moves a mostImportantThing to the top of the unfinished list
     *
     * @param id The ID of the mostImportantThing to move
     */
    public void moveToTop(int id) {
        this.mostImportantThingDao.prepend(this.mostImportantThingDao.find(id));
    }

    /**
     * Moves a mostImportantThing to the top of the finished list
     *
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
     *
     * @param index
     * @param context
     * @return index to insert mit at
     */
    private int orderInContext(int index, String context) {
        var ElementList = this.mostImportantThingDao.findAllMits();
        int numElems = ElementList.size();

        int completedIndex = index;

        //Find index of the first element where the next element is completed
        for (int i = index; i < numElems; i++) {
            if (ElementList.get(i).workContext.equals(context)) {
                if (ElementList.get(i).completed) {
                    break;
                }
                completedIndex ++;
            } else {
                break;
            }
        }
        //index is the start of the given context
        //completedIndex is the index with the last completed val of that context
        return completedIndex;
    }

    /**
     * Adds a new MostImportantThing to the repository
     *
     * @param mit The MostImportantThing being added
     */
    public void addNewMostImportantThing(MostImportantThing mit) {
        System.out.println("mit has context " + mit.workContext());
        //order goes: Home, Work, School, Errands
        var ElementList = this.mostImportantThingDao.findAllMits();

        ContextOrderer con = new ContextOrderer();

        int numElems = ElementList.size();
        int finalIndex = 0;

        if (numElems == 0) {
            this.mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mit));
            return;
        }

        for(int i = 0; i < numElems; i++) {
            if(con.compare(mit.workContext(), ElementList.get(i).workContext) == 0) {
                finalIndex = orderInContext(i, mit.workContext());
                break;
            } else if(con.compare(mit.workContext(), ElementList.get(i).workContext) < 0) {
                finalIndex = i;
                break;
            }

            if(i == numElems - 1) {finalIndex = numElems; }
        }

        if(finalIndex == numElems) {
            this.mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mit.withSortOrder(this.mostImportantThingDao.getMaxSortOrder() + 1)));
        } else {
            //Shift all completed MITs down, insert new one before them
            int sortOrder = ElementList.get(finalIndex).toMostImportantThing().sortOrder();
            this.mostImportantThingDao.shiftSortOrders(ElementList.get(finalIndex).toMostImportantThing().sortOrder(), this.mostImportantThingDao.getMaxSortOrder(), 1);
            this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(mit.withSortOrder(sortOrder)));
        }



    }

    /**
     * Adds a new MostImportantThing to the repository
     *
     * @param mit The MostImportantThing being added
     */
    public void addNewRecurringMostImportantThing(RecurringMostImportantThing mit) {
        this.mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(mit));
        this.updateRecurringMits();
        //**** For now just adding the new recurringMIT to the end ********
    }

    public void addNewPendingMostImportantThing(PendingMostImportantThing pendingMit) {
        this.mostImportantThingDao.append(MostImportantThingEntity.fromMostImportantThing(pendingMit));
        //For now just adding the new pendingMit to the end
    }

    /**
     * Adds a recurring MIT to tomorrow's view if tomorrow's date is the scheduled date
     * Will not add to tomorrow's view if already present
     */
    public void updateRecurringMits() {
        System.out.println("TestUpdate Updating recurring mits called!");
        var recurringMITs = mostImportantThingDao.findAllRecurrings();
        Date tomorrow = new Date(currDate.getTime() + TimeUnit.DAYS.toMillis(1));
        Date today = currDate;
        System.out.println("TestUpdate there are " + recurringMITs.size() + " recurrings!");
        for (var recurringMIT : recurringMITs) {
            RecurringMostImportantThing recurring = recurringMIT.toRecurringMostImportantThing();
            Date recurringDate = new Date(recurring.mit.timeCreated());
            Calendar recurrCal = Calendar.getInstance();
            recurrCal.setTime(recurringDate);
            Calendar refCal = Calendar.getInstance();
            refCal.setTime(today);
            boolean recurrDatePastToday = (recurrCal.get(Calendar.DAY_OF_YEAR) > refCal.get(Calendar.DAY_OF_YEAR)
                    && recurrCal.get(Calendar.YEAR) >= refCal.get(Calendar.YEAR));
            //Update refCal to be tomorrow
            refCal.setTime(new Date(today.getTime() + TimeUnit.DAYS.toMillis(1)));
            boolean recurrDatePastTomorrow = (recurrCal.get(Calendar.DAY_OF_YEAR) > refCal.get(Calendar.DAY_OF_YEAR)
                    && recurrCal.get(Calendar.YEAR) >= refCal.get(Calendar.YEAR));
            System.out.println("recurrDatePastToday is " + recurrDatePastToday + " and recurrDatePastTomorrow is " + recurrDatePastTomorrow);
            switch (recurringMIT.recurPeriod) {
                case "Daily":
                    if (!recurrDatePastTomorrow && !containsNormalMITInTomorrow(recurringMIT)) {
                        //If it doesn't have it, check if you need to add it
                        System.out.println("TestUpdate adding to today daily!");
                        this.addNewMostImportantThing(new MostImportantThing(null, recurringMIT.task, currDate.getTime() + TimeUnit.DAYS.toMillis(1), -1, recurringMIT.completed, recurringMIT.workContext));
                    }
                    if (!recurrDatePastToday && !containsNormalMIT(recurringMIT)) {
                        System.out.println("TestUpdate adding to tomorrow dai!");
                        this.addNewMostImportantThing(new MostImportantThing(null, recurringMIT.task, currDate.getTime(), -1, recurringMIT.completed, recurringMIT.workContext));
                    }
                    break;
                case "Weekly":
                    if (!recurrDatePastTomorrow && !containsNormalMITInTomorrow(recurringMIT) && sameDayOfWeek(tomorrow, recurringDate)) {
                        //If it doesn't have it in tomorrow, check if you need to add it
                        System.out.println("TestUpdate Doesn't contain in today!");
                        this.addNewMostImportantThing(new MostImportantThing(null, recurringMIT.task, currDate.getTime() + TimeUnit.DAYS.toMillis(1), -1, recurringMIT.completed, recurringMIT.workContext));
                    }
                    if (!recurrDatePastToday && !containsNormalMIT(recurringMIT) && sameDayOfWeek(today, recurringDate)) {
                        //If id doesn't have it in today, check if you need to add it
                        System.out.println("TestUpdate Doesn't contain in tomorrow!");
                        this.addNewMostImportantThing(new MostImportantThing(null, recurringMIT.task, currDate.getTime(), -1, recurringMIT.completed, recurringMIT.workContext));
                    }
                    break;
                case "Monthly":
                    System.out.println("Testing if should add a monthly MIT");
                    if (!recurrDatePastTomorrow && !containsNormalMITInTomorrow(recurringMIT) && sameWeekdayOfMonth(tomorrow, recurringDate)) {
                        //If it doesn't have it in tomorrow, check if you need to add it
                        System.out.println("TestUpdate Doesn't contain in today!");
                        this.addNewMostImportantThing(new MostImportantThing(null, recurringMIT.task, currDate.getTime() + TimeUnit.DAYS.toMillis(1), -1, recurringMIT.completed, recurringMIT.workContext));
                    }
                    if (!recurrDatePastToday && !containsNormalMIT(recurringMIT) && sameWeekdayOfMonth(today, recurringDate)) {
                        //If id doesn't have it in today, check if you need to add it
                        System.out.println("TestUpdate Doesn't contain in tomorrow!");
                        this.addNewMostImportantThing(new MostImportantThing(null, recurringMIT.task, currDate.getTime(), -1, recurringMIT.completed, recurringMIT.workContext));
                    }
                    break;
                case "Yearly":
                    if (!recurrDatePastTomorrow && !containsNormalMITInTomorrow(recurringMIT) && sameDayOfYear(tomorrow, recurringDate)) {
                        //If it doesn't have it in tomorrow, check if you need to add it
                        System.out.println("TestUpdate doesn't contain in today!");
                        this.addNewMostImportantThing(new MostImportantThing(null, recurringMIT.task, currDate.getTime() + TimeUnit.DAYS.toMillis(1), -1, recurringMIT.completed, recurringMIT.workContext));
                    }
                    if (!recurrDatePastToday && !containsNormalMIT(recurringMIT) && sameDayOfYear(today, recurringDate)) {
                        //If id doesn't have it in today, check if you need to add it
                        System.out.println("TestUpdate Doesn't contain in tomorrow!");
                        this.addNewMostImportantThing(new MostImportantThing(null, recurringMIT.task, currDate.getTime(), -1, recurringMIT.completed, recurringMIT.workContext));
                    }
                    break;
                default:
                    break;
            }
        }
//        this.removeDuplicates();
    }

    /**
     * Count of the number of things in the repository
     *
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

    private boolean sameWeekdayOfMonth(Date dateOne, Date dateTwo) {

        Calendar calOne = Calendar.getInstance();
        calOne.setTime(dateOne);
        int dayOfMonthCalOne = calOne.get(Calendar.DAY_OF_MONTH);
        int occurrenceOfDayCalOne = ((dayOfMonthCalOne - 1)/ 7);
        Calendar calTwo = Calendar.getInstance();
        calTwo.setTime(dateTwo);
        int dayOfMonthCalTwo = calTwo.get(Calendar.DAY_OF_MONTH);
        int occurrenceOfDayCalTwo = ((dayOfMonthCalTwo - 1)/ 7);
        System.out.println("sameDayOfMonth is " + (calOne.get(Calendar.DAY_OF_MONTH) == calTwo.get(Calendar.DAY_OF_MONTH)));
        System.out.println("dateOne is " + calOne.get(Calendar.DAY_OF_MONTH) + " and dateTwo is " + calTwo.get(Calendar.DAY_OF_MONTH));
        return ((occurrenceOfDayCalOne == occurrenceOfDayCalTwo) && calOne.get(Calendar.DAY_OF_WEEK) == calTwo.get(Calendar.DAY_OF_WEEK));

    }

    private boolean sameDayOfYear(Date dateOne, Date dateTwo) {
        Calendar calOne = Calendar.getInstance();
        calOne.setTime(dateOne);
        Calendar calTwo = Calendar.getInstance();
        calTwo.setTime(dateTwo);
        return (calOne.get(Calendar.DAY_OF_YEAR) == calTwo.get(Calendar.DAY_OF_YEAR));
    }

    private boolean containsNormalMIT(MostImportantThingEntity mitEntity) {
        var allMitEntities = this.findAllToday();
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
        var allMitEntities = this.findAllTomorrow();
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

    public List<MostImportantThingEntity> findAllToday() {
        var entityList = this.mostImportantThingDao.findAllMits();
        List<MostImportantThingEntity> outputList = new ArrayList<>();
        Date today = currDate;
        //Date tomorrow = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
        for (var entity : entityList) {
            var entityTime = entity.timeCreated;
            Date entityDate = new Date(entityTime);
            if (sameExactDay(entityDate, today)) {
                outputList.add(entity);
            }
        }
        return outputList;
    }

    public List<MostImportantThingEntity> findAllTomorrow() {
        var entityList = this.mostImportantThingDao.findAllMits();
        List<MostImportantThingEntity> outputList = new ArrayList<>();
        Date tomorrow = new Date(currDate.getTime() + TimeUnit.DAYS.toMillis(1));
        //Date tomorrow = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
        for (var entity : entityList) {
            var entityTime = entity.timeCreated;
            Date entityDate = new Date(entityTime);
            if (sameExactDay(entityDate, tomorrow)) {
                outputList.add(entity);
            }
        }
        return outputList;
    }

    public boolean sameExactDay(Date dateOne, Date dateTwo) {
        Calendar calOne = Calendar.getInstance();
        calOne.setTime(dateOne);
        Calendar calTwo = Calendar.getInstance();
        calTwo.setTime(dateTwo);
        if (calOne.get(Calendar.YEAR) == calTwo.get(Calendar.YEAR)
                && calOne.get(Calendar.MONTH) == calTwo.get(Calendar.MONTH)
                && calOne.get(Calendar.DAY_OF_MONTH) == calTwo.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    public boolean inSameTodaySlashTomorrowFragment(Date dateOne, Date dateTwo) {
        Calendar calOne = Calendar.getInstance();
        calOne.setTime(dateOne);
        Calendar calTwo = Calendar.getInstance();
        calTwo.setTime(dateTwo);
        Date today = currDate;
        Date tomorrow = new Date(currDate.getTime() + TimeUnit.DAYS.toMillis(1));
        if ((sameDayOfYear(tomorrow, calOne.getTime()) && sameDayOfYear(tomorrow, calTwo.getTime()))
                || ((sameDayOfYear(today, calOne.getTime()) || calOne.getTime().before(today)) && (sameDayOfYear(today, calTwo.getTime()) || calTwo.getTime().before(today)))) {
            return true;
        }
        return false;
    }

    public void setCurrDate(Date currDate) {
        this.currDate = currDate;
    }

    @Override
    public void moveToToday(PendingMostImportantThing pendingMit) {
        this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(
                pendingMit.convertToMit(currDate.getTime())));
                //pendingMit.convertToMit(System.currentTimeMillis())));
    }
    @Override
    public void moveToTomorrow(PendingMostImportantThing pendingMit) {
        this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(
                pendingMit.convertToMit(currDate.getTime() + TimeUnit.DAYS.toMillis(1))));
                //pendingMit.convertToMit(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1))));
    }
    @Override
    public void finishPending(PendingMostImportantThing pendingMit) {
        this.mostImportantThingDao.insert(MostImportantThingEntity.fromMostImportantThing(
                pendingMit.convertToMit(currDate.getTime()).withCompleted(true)));
                //pendingMit.convertToMit(System.currentTimeMillis()).withCompleted(true)));
    }

}

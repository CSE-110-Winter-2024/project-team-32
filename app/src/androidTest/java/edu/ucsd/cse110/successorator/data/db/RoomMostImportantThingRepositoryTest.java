package edu.ucsd.cse110.successorator.data.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.PendingMostImportantThing;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

public class RoomMostImportantThingRepositoryTest {

    private RoomMostImportantThingRepository repository;
    private MockMostImportantThingDao mockDao;
    private Date currDate;

    @Before
    public void setUp() {
        mockDao = new MockMostImportantThingDao();
        repository = new RoomMostImportantThingRepository(mockDao, new Date());
        currDate = new Date();
    }

    @Test
    public void testMoveToToday() {
        MostImportantThingEntity mitEntity = new MostImportantThingEntity(1, "task",
                currDate.getTime(), 0, true, true, false,
                null, "home");
        MostImportantThing mitExpected = new MostImportantThing(1, "task", currDate.getTime(),
                0, true, "home");
        PendingMostImportantThing pending = mitEntity.toPendingMostImportantThing();

        repository.moveToToday(pending);

        assertFalse(mockDao.getEntity(0).isPending);
        assertEquals(mitExpected, mockDao.getEntity(0).toMostImportantThing());
    }

    @Test
    public void testMoveToTomorrow() {
        MostImportantThingEntity mitEntity = new MostImportantThingEntity(1, "task",
                currDate.getTime(), 0, true, true, false,
                null, "home");
        MostImportantThing mitExpected = new MostImportantThing(1, "task", currDate.getTime() + TimeUnit.DAYS.toMillis(1),
                0, true, "home");
        PendingMostImportantThing pending = mitEntity.toPendingMostImportantThing();

        repository.moveToTomorrow(pending);

        assertFalse(mockDao.getEntity(0).isPending);
        assertEquals(mitExpected, mockDao.getEntity(0).toMostImportantThing());

    }

    @Test
    public void testRemoveCompletedTasks() {
        // Prepare a list of MostImportantThingEntity instances
        List<MostImportantThingEntity> entities = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // Set time for each entity to be before the cutoff (yesterday)
        calendar.add(Calendar.DATE, -1); // Set to yesterday
        long yesterdayTime = calendar.getTimeInMillis();
        entities.add(new MostImportantThingEntity(1, "Task 1", yesterdayTime, 1, true));
        entities.add(new MostImportantThingEntity(2, "Task 2", yesterdayTime, 2, false));
        entities.add(new MostImportantThingEntity(3, "Task 3", yesterdayTime, 3, true));

        // Assuming that these entities are the complete list from the database
        mockDao.setEntities(entities);

        // Execute the method under test
        repository.removeCompletedTasks();

        // Verify the correct tasks are removed
        assertTrue("Task 1 should be removed", mockDao.isRemoved(1));
        assertFalse("Task 2 should not be removed", mockDao.isRemoved(2));
        assertTrue("Task 3 should be removed", mockDao.isRemoved(3));
    }

    @Test
    public void testRemoveCompletedTasksAfter2AM() {
        // Initialize the mock data with different scenarios
        // Create tasks with creation times set for today but completed yesterday
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoAMToday = now.withHour(2).withMinute(0).withSecond(0);
        long timeYesterday = twoAMToday.minusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long timeTodayAfter2AM = twoAMToday.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        List<MostImportantThingEntity> entities = new ArrayList<>();
        entities.add(new MostImportantThingEntity(1, "Completed Task Yesterday", timeYesterday, 1, true));
        entities.add(new MostImportantThingEntity(2, "Incomplete Task Yesterday", timeYesterday, 2, false));
        entities.add(new MostImportantThingEntity(3, "Completed Task Today After 2AM", timeTodayAfter2AM, 3, true));

        // Assuming these entities represent the complete list from the database
        mockDao.setEntities(entities);

        // Simulate running the cleanup after 2 AM today
        repository.removeCompletedTasks(twoAMToday.plusMinutes(1));  // Uses LocalDateTime.now() equivalent for test

        // Verify that only the appropriate task(s) are removed
        assertTrue("Completed Task Yesterday should be removed", mockDao.isRemoved(1));
        assertFalse("Incomplete Task Yesterday should not be removed", mockDao.isRemoved(2));
        assertFalse("Completed Task Today After 2AM should not be removed", mockDao.isRemoved(3));
    }

    // Implement your mock DAO as an inner class or separate class
    static class MockMostImportantThingDao implements MostImportantThingDao {
        private List<MostImportantThingEntity> entities = new ArrayList<>();
        private List<Integer> removedIds = new ArrayList<>();

        void setEntities(List<MostImportantThingEntity> entities) {
            this.entities = entities;
        }

        public MostImportantThingEntity getEntity(int i) {
            return entities.get(i);
        }

        boolean isRemoved(int id) {
            return removedIds.contains(id);
        }

        public boolean contains(MostImportantThingEntity mit) {
            return entities.contains(mit);
        }

        @Override
        public Long insert(MostImportantThingEntity mostImportantThing) {
            entities.add(mostImportantThing);
            return null;
        }

        @Override
        public List<Long> insert(List<MostImportantThingEntity> mostImportantThings) {
            return null;
        }

        @Override
        public MostImportantThingEntity find(int id) {
            return null;
        }

        @Override
        public List<MostImportantThingEntity> findAllMits() {
            return entities;
        }

        @Override
        public LiveData<List<MostImportantThingEntity>> findAllNormalAsLiveData() {
            return null;
        }

        @Override
        public LiveData<List<MostImportantThingEntity>> findAllPendingAsLiveData() {
            return null;
        }

        @Override
        public LiveData<List<MostImportantThingEntity>> findAllOfContextAsLiveData(String Context) {return null;}

        @Override
        public LiveData<List<MostImportantThingEntity>> findAllRecurringAsLiveData() {
            return null;
        }

        public List<MostImportantThingEntity> findAllOfContext(String context) {return null;}

        public List<MostImportantThingEntity> findAllPendings() {
            return null;
        }

        public List<MostImportantThingEntity> findAllRecurrings() {
            return null;
        }
        public List<MostImportantThingEntity> findAll() {
            return null;
        }

        @Override
        public LiveData<MostImportantThingEntity> findAsLiveData(int id) {
            return null;
        }

        @Override
        public LiveData<List<MostImportantThingEntity>> findAllAsLiveData() {
            return null;
        }

        public LiveData<List<MostImportantThingEntity>> findAllPendingsAsLiveData() {
            return null;
        }

        public LiveData<List<MostImportantThingEntity>> findAllRecurringsAsLiveData() {
            return null;
        }

        @Override
        public int count() {
            return 0;
        }

        @Override
        public int getMinSortOrder() {
            return 0;
        }

        @Override
        public int getMaxSortOrder() {
            return 0;
        }

        @Override
        public void shiftSortOrders(int from, int to, int by) {

        }

        @Override
        public void delete(int id) {
            removedIds.add(id); // Track removed IDs for verification
            entities.removeIf(entity -> entity.id == id);
        }

        @Override
        public void toggleCompleted(int id) {

        }

        @Override
        public void clear() {

        }

    }
}


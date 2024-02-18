package edu.ucsd.cse110.successorator.data.db;

import static org.junit.Assert.*;

import androidx.lifecycle.LiveData;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class RoomMostImportantThingRepositoryTest {

    private RoomMostImportantThingRepository repository;
    private MockMostImportantThingDao mockDao;

    @Before
    public void setUp() {
        mockDao = new MockMostImportantThingDao();
        repository = new RoomMostImportantThingRepository(mockDao);
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

    // Implement your mock DAO as an inner class or separate class
    static class MockMostImportantThingDao implements MostImportantThingDao {
        private List<MostImportantThingEntity> entities = new ArrayList<>();
        private List<Integer> removedIds = new ArrayList<>();

        void setEntities(List<MostImportantThingEntity> entities) {
            this.entities = entities;
        }

        boolean isRemoved(int id) {
            return removedIds.contains(id);
        }

        @Override
        public Long insert(MostImportantThingEntity mostImportantThing) {
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
        public List<MostImportantThingEntity> findAll() {
            return entities;
        }

        @Override
        public LiveData<MostImportantThingEntity> findAsLiveData(int id) {
            return null;
        }

        @Override
        public LiveData<List<MostImportantThingEntity>> findAllAsLiveData() {
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


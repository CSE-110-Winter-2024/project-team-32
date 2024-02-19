package edu.ucsd.cse110.successorator.data.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;


/**
 * Tests all of the methods in the MostImportantThingDao interface
 * to make sure they adjust the database appropriately
 */
@RunWith(AndroidJUnit4.class)
public class SuccessoratorDatabaseTest {
    private SuccessoratorDatabase db;
    private MostImportantThingDao mitDao;
    private MostImportantThingEntity mit0, mit1, mit2, mit3;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        this.db = Room.inMemoryDatabaseBuilder(context, SuccessoratorDatabase.class).build();
        this.mitDao = this.db.mostImportantThingDao();
    }
    @After
    public void closeDb() throws IOException {
        this.db.close();
    }

    /**
     * Helper method to initialize all of the mits
     */
    private void initializeMitEntities() {
        mit0 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(0, "task0", 0L, 0, false));

        mit1 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(1, "task1", 0L, 2, false));
        mit2 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(2, "task2", 0L, 3, false));

        mit3 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(3, "task3", 0L, 5, false));
    }

    private void prependAllMitEntities() {
        this.mitDao.prepend(mit0);
        this.mitDao.prepend(mit1);
        this.mitDao.prepend(mit2);
        this.mitDao.prepend(mit3);
    }

    private List<String> getAllTasks() {
        return mitDao.findAll().stream()
                .map(MostImportantThingEntity::toMostImportantThing)
                .map(MostImportantThing::task)
                .collect(Collectors.toList());
    }

    /**
     * Tests inserting and then finding 10 MITs with different ids
     */
    @Test
    public void testInsertAndFind() {
        MostImportantThing actual;
        MostImportantThing expected;
        for (int i = 0; i < 10; i++) {
            var mit = new MostImportantThing(i, "task1", 0L, 0, false);
            var mitEntity = MostImportantThingEntity.fromMostImportantThing(mit);
            this.mitDao.insert(mitEntity);
            actual = mitDao.find(i).toMostImportantThing();

            expected = new MostImportantThing(i, "task1", 0L, 0, false);
            assertEquals(expected, actual);
        }
    }

    /**
     * Tests inserting 10 different tasks and then calling findAll
     */
    @Test
    public void testInsertAndFindAll() {
        List<MostImportantThingEntity> expectedList = new ArrayList<>();
        for (int i=0; i<10; i++) {
            var mit = new MostImportantThing(i, "task1", 0L, 0, false);
            var mitEntity = MostImportantThingEntity.fromMostImportantThing(mit);
            this.mitDao.insert(mitEntity);
            expectedList.add(mitEntity);
        }
        List<MostImportantThingEntity> actualList = mitDao.findAll();

        assertEquals(expectedList.size(), mitDao.count());

        for (int i=0; i<actualList.size(); i++) {
            assertEquals(expectedList.get(i).toMostImportantThing(),
                         actualList.get(i).toMostImportantThing());
        }
    }

    /**
     * Tests inserting an item with the same ID as an item that's already in the
     */
    @Test
    public void testReplaceAndFind() {
        this.initializeMitEntities();
        this.mitDao.insert(mit0);
        mit1 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(0, "task2", 0L, 3, false));
        this.mitDao.insert(mit1);

        assertEquals(1, this.mitDao.findAll().size());
    }

    @Test
    public void testGetMinMaxSortOrder() {
        this.initializeMitEntities();

        List<MostImportantThingEntity> toAdd = Arrays.asList(mit0, mit1, mit2, mit3);
        this.mitDao.insert(toAdd);

        assertEquals(5, this.mitDao.getMaxSortOrder());
        assertEquals(0, this.mitDao.getMinSortOrder());
        // upsert the max and min
        this.mitDao.insert(
                MostImportantThingEntity.fromMostImportantThing(
                        new MostImportantThing(0, "task1", 0L, 1, false))
        );
        this.mitDao.insert(
                MostImportantThingEntity.fromMostImportantThing(
                        new MostImportantThing(3, "task1", 0L, 4, false))
        );
        assertEquals(4, this.mitDao.getMaxSortOrder());
        assertEquals(1, this.mitDao.getMinSortOrder());
    }

    @Test
    public void testAppend() {
        this.initializeMitEntities();

        // defined sort orders shouldn't matter
        this.mitDao.append(mit0);
        this.mitDao.append(mit1);
        this.mitDao.append(mit2);
        this.mitDao.append(mit3);

        List<String> actualTasks = getAllTasks();

        List<String> expectedTasks = Arrays.asList("task0", "task1", "task2", "task3");

        assertEquals(actualTasks, expectedTasks);
    }
    @Test
    public void testPrepend() {
        this.initializeMitEntities();
        // defined sort orders shouldn't matter
        this.prependAllMitEntities();

        List<String> actualTasks = getAllTasks();

        List<String> expectedTasks = Arrays.asList("task3", "task2", "task1", "task0");

        assertEquals(actualTasks, expectedTasks);
    }

    @Test
    public void testDelete() {
        this.initializeMitEntities();
        // defined sort orders shouldn't matter
        this.prependAllMitEntities();

        this.mitDao.delete(0);
        this.mitDao.delete(1);
        this.mitDao.delete(2);
        this.mitDao.delete(3);

        assertEquals(0, mitDao.count());
    }

    @Test
    public void testToggleCompleted() {
        this.initializeMitEntities();
        // defined sort orders shouldn't matter
        this.prependAllMitEntities();

        this.mitDao.toggleCompleted(1);
        this.mitDao.toggleCompleted(3);

        List<MostImportantThing> actualTasks = mitDao.findAll().stream()
                .map(MostImportantThingEntity::toMostImportantThing)
                .collect(Collectors.toList());
        //MITs come out in reverse order since we used prepend
        assertTrue(actualTasks.get(0).completed());
        assertFalse(actualTasks.get(1).completed());
        assertTrue(actualTasks.get(2).completed());
        assertFalse(actualTasks.get(3).completed());

        this.mitDao.toggleCompleted(0);
        this.mitDao.toggleCompleted(1);
        this.mitDao.toggleCompleted(2);
        this.mitDao.toggleCompleted(3);

        actualTasks = mitDao.findAll().stream()
                .map(MostImportantThingEntity::toMostImportantThing)
                .collect(Collectors.toList());

        assertFalse(actualTasks.get(0).completed());
        assertTrue(actualTasks.get(1).completed());
        assertFalse(actualTasks.get(2).completed());
        assertTrue(actualTasks.get(3).completed());
    }

}
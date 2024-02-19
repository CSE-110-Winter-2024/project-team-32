package edu.ucsd.cse110.successorator.data.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.widget.TextClock;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThingRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MostImportantThingRepositoryTest {
    private SuccessoratorDatabase db;
    private MostImportantThingDao mitDao;
    private MostImportantThingRepository mitRepo;
    private MostImportantThing mit0, mit1, mit2, mit3;
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        this.db = Room.inMemoryDatabaseBuilder(context, SuccessoratorDatabase.class).build();
        this.mitDao = this.db.mostImportantThingDao();
        this.mitRepo = new RoomMostImportantThingRepository(mitDao);
    }
    @After
    public void closeDb() throws IOException {
        this.db.close();
    }

    private void initializeMits() {
        mit0 = new MostImportantThing(0, "task0", 0L, 0, false);
        mit1 = new MostImportantThing(1, "task1", 0L, 2, false);
        mit2 = new MostImportantThing(2, "task2", 0L, 3, false);
        mit3 = new MostImportantThing(3, "task3", 0L, 5, false);
    }

    private void prependAllMits() {
        this.mitRepo.prepend(mit0);
        this.mitRepo.prepend(mit1);
        this.mitRepo.prepend(mit2);
        this.mitRepo.prepend(mit3);
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
    public void testAppend() {
        this.initializeMits();

        // defined sort orders shouldn't matter
        this.mitRepo.append(mit0);
        this.mitRepo.append(mit1);
        this.mitRepo.append(mit2);
        this.mitRepo.append(mit3);

        List<String> actualTasks = getAllTasks();

        List<String> expectedTasks = Arrays.asList("task0", "task1", "task2", "task3");

        assertEquals(actualTasks, expectedTasks);
    }

    @Test
    public void testPrepend() {
        this.initializeMits();
        // defined sort orders shouldn't matter
        this.prependAllMits();

        List<String> actualTasks = getAllTasks();

        List<String> expectedTasks = Arrays.asList("task3", "task2", "task1", "task0");

        assertEquals(actualTasks, expectedTasks);
    }

    @Test
    public void testDelete() {
        this.initializeMits();
        // defined sort orders shouldn't matter
        this.prependAllMits();

        this.mitRepo.remove(0);
        this.mitRepo.remove(1);
        this.mitRepo.remove(2);
        this.mitRepo.remove(3);

        assertEquals(0, mitDao.count());
    }

    @Test
    public void testDelete2() {
        this.initializeMits();
        // defined sort orders shouldn't matter
        this.prependAllMits();

        this.mitRepo.remove(0);
        this.mitRepo.remove(2);

        List<String> actualTasks = getAllTasks();

        List<String> expectedTasks = Arrays.asList("task1", "task3");


        assertEquals(actualTasks, expectedTasks);
    }

    @Test
    public void testMoveToTop() {
        initializeMits();
        // defined sort orders shouldn't matter
        this.prependAllMits();

        this.mitRepo.moveToTop(mit0.id());

        List<String> actualTasks = getAllTasks();
        assertEquals(4, actualTasks.size());

        List<String> expectedTasks = Arrays.asList("task0", "task3", "task2", "task1");
        assertEquals(expectedTasks, actualTasks);

        this.mitRepo.moveToTop(mit2.id());

        actualTasks = getAllTasks();
        assertEquals(4, actualTasks.size());

        expectedTasks = Arrays.asList("task2", "task0", "task3", "task1");
        assertEquals(expectedTasks, actualTasks);

    }

    @Test
    public void testAddNewMitEmpty() {
        this.initializeMits();
        this.mitRepo.addNewMostImportantThing(mit0);
        List<String> actualTasks = getAllTasks();
        List<String> expectedTasks = Arrays.asList("task0");
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void testAddNewMitOnlyFinished() {
        this.initializeMits();
        this.mit0.setCompleted(true);
        this.mit1.setCompleted(true);
        this.mit2.setCompleted(true);
        this.mit3.setCompleted(true);
        this.prependAllMits();
        MostImportantThing mit4 = new MostImportantThing(10, "task4", 0L, 5, false);
        this.mitRepo.addNewMostImportantThing(mit4);
        List<String> actualTasks = getAllTasks();
        List<String> expectedTasks = Arrays.asList("task4", "task3", "task2", "task1", "task0");
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void testAddNewMitNoFinished() {
        this.initializeMits();
        this.prependAllMits();
        MostImportantThing mit4 = new MostImportantThing(10, "task4", 0L, 5, false);
        this.mitRepo.addNewMostImportantThing(mit4);
        List<String> actualTasks = getAllTasks();
        List<String> expectedTasks = Arrays.asList("task3", "task2", "task1", "task0", "task4");
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void testAddNewMitMixOfFinished() {
        this.initializeMits();
        this.mit0.setCompleted(true);
        this.mit1.setCompleted(true);
        this.prependAllMits();
        MostImportantThing mit4 = new MostImportantThing(10, "task4", 0L, 5, false);
        this.mitRepo.addNewMostImportantThing(mit4);
        List<String> actualTasks = getAllTasks();
        List<String> expectedTasks = Arrays.asList("task3", "task2", "task4", "task1", "task0");
        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    public void testMoveToTopOfFinishedOnlyFinished() {
        this.initializeMits();
        mit0.setCompleted(true);
        mit1.setCompleted(true);
        mit2.setCompleted(true);
        // defined sort orders shouldn't matter
        this.prependAllMits();

        this.mitRepo.moveToTopOfFinished(mit3.id());

        List<String> actualTasks = getAllTasks();
        assertEquals(4, actualTasks.size());

        List<String> expectedTasks = Arrays.asList("task3", "task2", "task1", "task0");
        assertEquals(expectedTasks, actualTasks);

    }

    @Test
    public void testMoveToTopOfFinishedHasFinished() {
        this.initializeMits();
        mit0.setCompleted(true);
        mit1.setCompleted(true);
        // defined sort orders shouldn't matter
        this.prependAllMits();

        this.mitRepo.moveToTopOfFinished(mit3.id());

        List<String> actualTasks = getAllTasks();
        assertEquals(4, actualTasks.size());

        List<String> expectedTasks = Arrays.asList("task2", "task3", "task1", "task0");
        assertEquals(expectedTasks, actualTasks);

        this.mitRepo.moveToTopOfFinished(mit2.id());

        actualTasks = getAllTasks();
        assertEquals(4, actualTasks.size());

        expectedTasks = Arrays.asList("task3", "task2", "task1", "task0");
        assertEquals(expectedTasks, actualTasks);

    }

    @Test
    public void testMoveToTopOfFinishedNoFinished() {
        this.initializeMits();
        // defined sort orders shouldn't matter
        this.prependAllMits();

        this.mitRepo.moveToTopOfFinished(mit3.id());

        List<String> actualTasks = getAllTasks();
        assertEquals(4, actualTasks.size());

        List<String> expectedTasks = Arrays.asList("task2", "task1", "task0", "task3");
        assertEquals(expectedTasks, actualTasks);

        this.mitRepo.moveToTopOfFinished(mit2.id());

        actualTasks = getAllTasks();
        assertEquals(4, actualTasks.size());

        expectedTasks = Arrays.asList("task1", "task0", "task3", "task2");
        assertEquals(expectedTasks, actualTasks);

    }

    @Test
    public void testToggleCompletedNoCompleted() {
        this.initializeMits();
        this.prependAllMits();
        this.mitRepo.toggleCompleted(mit3.id());

        //mit3 should be completed
        boolean actualCompleted = this.mitDao.find(mit3.id()).completed;
        assertTrue(actualCompleted);

        //mit3 should be reordered to the bottom of the list
        List<String> actualTasks = getAllTasks();
        List<String> expectedTasks = Arrays.asList("task2", "task1", "task0", "task3");

    }

    @Test
    public void testClear() {
        this.initializeMits();
        this.prependAllMits();
        this.mitRepo.clear();
        assertEquals(0, this.mitDao.findAll().size());
    }

    @Test
    public void testCount() {
        this.initializeMits();
        this.prependAllMits();
        assertEquals(4, this.mitRepo.count());
    }

    @Test
    public void testSaveList() {
        this.initializeMits();
        List<MostImportantThing> mitList = List.of(mit0, mit1, mit2, mit3);
        this.mitRepo.save(mitList);
        assertEquals(4, this.mitRepo.count());
    }

    @Test
    public void testSave() {
        this.initializeMits();
        this.mitRepo.save(mit0);
        assertEquals(1, this.mitRepo.count());
    }

}

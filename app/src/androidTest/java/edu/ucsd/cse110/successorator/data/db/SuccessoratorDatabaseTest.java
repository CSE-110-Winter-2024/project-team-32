package edu.ucsd.cse110.successorator.data.db;

import static org.junit.Assert.assertEquals;

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


@RunWith(AndroidJUnit4.class)
public class SuccessoratorDatabaseTest {
//    private MostImportantThingDao mitDao;
    private SuccessoratorDatabase db;
    private MostImportantThingDao mitDao;
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
    @Test
    public void insertAndFind() {
        MostImportantThing actual;
        MostImportantThing expected;
        for (int i=0; i<10; i++) {
            var mit = new MostImportantThing(i, "task1", 0L, 0);
            var mitEntity = MostImportantThingEntity.fromMostImportantThing(mit);
            this.mitDao.insert(mitEntity);
            actual = mitDao.find(i).toMostImportantThing();

            expected = new MostImportantThing(i, "task1", 0L, 0);
            assertEquals(expected, actual);
        }
    }
    @Test
    public void insertAndFindAll() {
        List<MostImportantThingEntity> expectedList = new ArrayList<>();
        for (int i=0; i<10; i++) {
            var mit = new MostImportantThing(i, "task1", 0L, 0);
            var mitEntity = MostImportantThingEntity.fromMostImportantThing(mit);
            this.mitDao.insert(mitEntity);
            expectedList.add(mitEntity);
        }
        List<MostImportantThingEntity> actualList = mitDao.findAll();

        assertEquals(expectedList.size(), mitDao.count());

        // should stream this instead
        for (int i=0; i<actualList.size(); i++) {
            assertEquals(expectedList.get(i).toMostImportantThing(),
                         actualList.get(i).toMostImportantThing());
        }
    }
    @Test
    public void replaceAndFind() {
        var mit0 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(0, "task1", 0L, 0));

        var mit1 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(0, "task1", 0L, 1));

        this.mitDao.insert(mit0);
        this.mitDao.insert(mit1);

        assertEquals(1, this.mitDao.findAll().size());
    }

    @Test
    public void getMinMaxSortOrder() {
        var mit0 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(0, "task1", 0L, 0));

        var mit1 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(1, "task1", 0L, 2));
        var mit2 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(2, "task1", 0L, 3));

        var mit3 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(3, "task1", 0L, 5));

        List<MostImportantThingEntity> toAdd = Arrays.asList(mit0, mit1, mit2, mit3);
        this.mitDao.insert(toAdd);

        assertEquals(5, this.mitDao.getMaxSortOrder());
        assertEquals(0, this.mitDao.getMinSortOrder());
        // upsert the max and min
        this.mitDao.insert(
                MostImportantThingEntity.fromMostImportantThing(
                        new MostImportantThing(0, "task1", 0L, 1))
        );
        this.mitDao.insert(
                MostImportantThingEntity.fromMostImportantThing(
                        new MostImportantThing(3, "task1", 0L, 4))
        );
        assertEquals(4, this.mitDao.getMaxSortOrder());
        assertEquals(1, this.mitDao.getMinSortOrder());
    }

    @Test
    public void appendTest() {
        var mit0 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(0, "task0", 0L, 0));

        var mit1 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(1, "task1", 0L, 5));
        var mit2 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(2, "task2", 0L, 3));

        var mit3 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(3, "task3", 0L, 5));

        // defined sort orders shouldn't matter
        this.mitDao.append(mit0);
        this.mitDao.append(mit1);
        this.mitDao.append(mit2);
        this.mitDao.append(mit3);

        List<String> actualTasks = mitDao.findAll().stream()
                .map(MostImportantThingEntity::toMostImportantThing)
                .map(MostImportantThing::task)
                .collect(Collectors.toList());

        List<String> expectedTasks = Arrays.asList("task0", "task1", "task2", "task3");

        assertEquals(actualTasks, expectedTasks);
    }
    @Test
    public void prependTest() {
        var mit0 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(0, "task0", 0L, 0));

        var mit1 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(1, "task1", 0L, 5));
        var mit2 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(2, "task2", 0L, 3));

        var mit3 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(3, "task3", 0L, 5));

        // defined sort orders shouldn't matter
        this.mitDao.prepend(mit0);
        this.mitDao.prepend(mit1);
        this.mitDao.prepend(mit2);
        this.mitDao.prepend(mit3);

        List<String> actualTasks = mitDao.findAll().stream()
                .map(MostImportantThingEntity::toMostImportantThing)
                .map(MostImportantThing::task)
                .collect(Collectors.toList());

        List<String> expectedTasks = Arrays.asList("task3", "task2", "task1", "task0");

        assertEquals(actualTasks, expectedTasks);
    }

    @Test
    public void deleteTest() {
        var mit0 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(0, "task0", 0L, 0));

        var mit1 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(1, "task1", 0L, 5));
        var mit2 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(2, "task2", 0L, 3));

        var mit3 = MostImportantThingEntity.fromMostImportantThing(
                new MostImportantThing(3, "task3", 0L, 5));

        // defined sort orders shouldn't matter
        this.mitDao.prepend(mit0);
        this.mitDao.prepend(mit1);
        this.mitDao.prepend(mit2);
        this.mitDao.prepend(mit3);

        this.mitDao.delete(0);
        this.mitDao.delete(1);
        this.mitDao.delete(2);
        this.mitDao.delete(3);

        assertEquals(0, mitDao.count());
    }

}
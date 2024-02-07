package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class MostImportantThingTest {
    @Test
    public void testGetters() {
        var mit = new MostImportantThing(1, "todo", 2024L, 0);
        assertEquals(Integer.valueOf(1), mit.id());
        assertEquals("todo", mit.task());
        assertEquals(2024L, mit.timeCreated(), 0);
        assertEquals(0, mit.sortOrder());
        assertEquals(true, mit.completed());
        mit.setCompleted(false);
        assertEquals(false, mit.completed());
    }

    @Test
    public void testWithId() {
        var mit = new MostImportantThing(1, "todo", 2024L, 0);
        var expected = new MostImportantThing(42, "todo", 2024L, 0);

        var actual = mit.withId(42);

        assertEquals(expected, actual);
    }

    @Test
    public void withSortOrder() {
        var mit = new MostImportantThing(1, "todo", 2024L, 0);
        var expected = new MostImportantThing(1, "todo", 2024L, 42);

        var actual = mit.withSortOrder(42);

        assertEquals(expected, actual);
    }
    @Test
    public void testEquals() {
        var mit1 = new MostImportantThing(1, "todo", 2024L, 0);
        var mit2 = new MostImportantThing(1, "todo", 2024L, 0);
        var mit3 = new MostImportantThing(2, "todo", 2024L, 0);

        assertEquals(mit1, mit2);
        assertNotEquals(mit1, mit3);
    }
}

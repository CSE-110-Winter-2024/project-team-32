package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MostImportantThingsTest {

    @Test
    public void testRotateForward() {
        var mits = new ArrayList<>(List.of(
                new MostImportantThing(1, "front1", 0L, 1, false),
                new MostImportantThing(2, "front2", 1L, 2, false),
                new MostImportantThing(3, "front3", 2L, 3, false)
        ));

        var expected = new ArrayList<>(List.of(
                new MostImportantThing(1, "front1", 0L, 3, false),
                new MostImportantThing(2, "front2", 1L, 1, false),
                new MostImportantThing(3, "front3", 2L, 2, false)
        ));

        var actual = MostImportantThings.rotate(mits, -1);

        assertEquals(expected, actual);
    }
    @Test
    public void testRotateBackward() {
        var mits = new ArrayList<>(List.of(
                new MostImportantThing(1, "front1", 0L, 1, false),
                new MostImportantThing(2, "front2", 1L, 2, false),
                new MostImportantThing(3, "front3", 2L, 3, false)
        ));

        var expected = new ArrayList<>(List.of(
                new MostImportantThing(1, "front1", 0L, 2, false),
                new MostImportantThing(2, "front2", 1L, 3, false),
                new MostImportantThing(3, "front3", 2L, 1, false)
        ));

        var actual = MostImportantThings.rotate(mits, 1);

        assertEquals(expected, actual);
    }
}
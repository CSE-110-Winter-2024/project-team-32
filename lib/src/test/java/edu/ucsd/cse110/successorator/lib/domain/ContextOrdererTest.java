package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ContextOrdererTest {

    @Test
    public void test() {
        ContextOrderer con = new ContextOrderer();
        assertEquals(0, con.compare("Home", "Home"));
        assertTrue(con.compare("Home", "Work") < 0);
        assertTrue(con.compare("Home", "School") < 0);
        assertTrue(con.compare("Home", "Errands") < 0);
        assertTrue(con.compare("Work", "School") < 0);
        assertTrue(con.compare("Work", "Errands") < 0);
        assertTrue(con.compare("Work", "Home") > 0);
        assertTrue(con.compare("School", "Errands") < 0);
        assertTrue(con.compare("Errands", "School") > 0);

    }


}

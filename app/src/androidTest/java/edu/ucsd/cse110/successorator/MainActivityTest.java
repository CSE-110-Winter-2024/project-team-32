package edu.ucsd.cse110.successorator;

import static org.junit.Assert.assertEquals;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    /*
    @Test
    public void displaysHelloWorld() {
        try (var scenario = ActivityScenario.launch(MainActivity.class)) {

            // Observe the scenario's lifecycle to wait until the activity is created.
            scenario.onActivity(activity -> {
                var rootView = activity.findViewById(R.id.root);
                var binding = ActivityMainBinding.bind(rootView);

                var expected = activity.getString(R.string.hello_world);
                var actual = binding.placeholderText.getText();

                assertEquals(expected, actual);
            });

            // Simulate moving to the started state (above will then be called).
            scenario.moveToState(Lifecycle.State.STARTED);
        }
    }
    */
    @Test
    public void displaysList() {
        try (var scenario = ActivityScenario.launch(MainActivity.class)) {

            // Observe the scenario's lifecycle to wait until the activity is created.
            scenario.onActivity(activity -> {
                var rootView = activity.findViewById(R.id.root);
                var binding = ActivityMainBinding.bind(rootView);
                String expected;
                MostImportantThing actual;
                for (int i=0; i<6; i++) {
                    expected = "todo" + (i+1);

                    actual = (MostImportantThing) binding.mitList.getItemAtPosition(i);

                    assertEquals(expected, actual.task());
                }
            });

            // Simulate moving to the started state (above will then be called).
            scenario.moveToState(Lifecycle.State.STARTED);
        }
    }
}
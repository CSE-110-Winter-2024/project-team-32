package edu.ucsd.cse110.successorator;

import static org.junit.Assert.assertEquals;

import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Test
    public void displaysBlankMessageText() {
        try (var scenario = ActivityScenario.launch(MainActivity.class)) {

            // Observe the scenario's lifecycle to wait until the activity is created.
            scenario.onActivity(activity -> {
                var rootView = activity.findViewById(R.id.root);
                var binding = ActivityMainBinding.bind(rootView);

                var expected = activity.getString(R.string.blank_message_text);
                TextView actualTextView = binding.getRoot().getViewById(R.id.fragment_container).findViewById(R.id.blank_message_text);
                var actual = actualTextView.getText().toString();
                //getRoot().blankMessageText.getText();//       .getText();///placeholderText.getText();

                assertEquals(expected, actual);
            });

            // Simulate moving to the started state (above will then be called).
            scenario.moveToState(Lifecycle.State.STARTED);
        }
    }

    @Test
    public void displaysBlankMsgWithNoList() {
        try (var scenario = ActivityScenario.launch(MainActivity.class)) {

            // Observe the scenario's lifecycle to wait until the activity is created.
            scenario.onActivity(activity -> {
                var rootView = activity.findViewById(R.id.root);
                var binding = ActivityMainBinding.bind(rootView);

                // make sure list is empty
                int expectedCount = 0;
                int actualCount = ((ListView)( binding.getRoot().getViewById(R.id.fragment_container)).findViewById(R.id.mit_list)).getCount();

                assertEquals(expectedCount, actualCount);

                // make sure message is displayed
                assertEquals(activity.getString(R.string.blank_message_text),
                        ((TextView) binding.getRoot().findViewById(R.id.blank_message_text)).getText());


            });
            // Simulate moving to the started state (above will then be called).
            scenario.moveToState(Lifecycle.State.STARTED);
        }
    }
}

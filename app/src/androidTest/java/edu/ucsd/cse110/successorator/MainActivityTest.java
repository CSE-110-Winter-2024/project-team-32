package edu.ucsd.cse110.successorator;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.runner.RunWith;

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
//    @Test
//    public void displaysBlankMsgWithNoList() {
//        try (var scenario = ActivityScenario.launch(MainActivity.class)) {
//
//            // Observe the scenario's lifecycle to wait until the activity is created.
//            scenario.onActivity(activity -> {
//                var rootView = activity.findViewById(R.id.root);
//                var binding = ActivityMainBinding.bind(rootView);
//
//                // make sure list is empty
//                int expectedCount = 0;
//                int actualCount = binding.mitList.getCount();
//
//                assertEquals(expectedCount, actualCount);
//
//                // make sure message is displayed
//                assertEquals(activity.getString(R.string.blank_message_text),
//                        binding.blankMessageText.getText());
//
//
//            });
//            // Simulate moving to the started state (above will then be called).
//            scenario.moveToState(Lifecycle.State.STARTED);
//        }
//    }
}

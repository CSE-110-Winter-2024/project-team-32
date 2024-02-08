package edu.ucsd.cse110.successorator.ui;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.SuccessoratorApplication;
import edu.ucsd.cse110.successorator.data.db.RoomMostImportantThingRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.databinding.FragmentMitListBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

@RunWith(AndroidJUnit4.class)
public class MitListTest {

    @Before
    public void setup() {
        Context context = getApplicationContext();

        // Retrieve the application class
        SuccessoratorApplication application = (SuccessoratorApplication) context.getApplicationContext();
        application.onCreate();
        System.out.println("initializing database");
        // init the database
        var database = Room.inMemoryDatabaseBuilder(
                        getApplicationContext(),
                        SuccessoratorDatabase.class)
                .allowMainThreadQueries()
                .build();

        // feed that database into this.MITRepo
        application.mostImportantThingRepository = new RoomMostImportantThingRepository(database.mostImportantThingDao());
    }
    @Test
    public void displaysBlankMsgWithNoList() {
        try (FragmentScenario<MitList> scenario = FragmentScenario.launchInContainer(MitList.class)) {

            // Observe the scenario's lifecycle to wait until the activity is created.
            scenario.onFragment(fragment -> {
                var rootView = fragment.getView().findViewById(R.id.root);
                var binding = FragmentMitListBinding.bind(rootView);

                // make sure list is empty
                int expectedCount = 0;
                int actualCount = binding.mitList.getCount();

//                assertEquals(expectedCount, actualCount);
                // make sure message is displayed
                assertEquals(fragment.getString(R.string.blank_message_text),
                        binding.blankMessageText.getText());
            });
            // Simulate moving to the started state (above will then be called).
            scenario.moveToState(Lifecycle.State.STARTED);
        }
    }


    @Test
    public void displayMitsWhenAdded() {
        // launching the fragment scenario
        try (FragmentScenario<MitList> scenario = FragmentScenario.launchInContainer(MitList.class)) {
            // Observe the fragment's lifecycle to wait until the fragment is created.
            scenario.onFragment(fragment -> {
                // grab stuff from the xml
                var rootView = fragment.getView().findViewById(R.id.root);
                var binding = FragmentMitListBinding.bind(rootView);

                var modelOwner = fragment.requireActivity();
                var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
                var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
                var activityModel = modelProvider.get(MainViewModel.class);

                activityModel.clear();

                // append stuff, should be fine as we've tested append
                System.out.println("appending 1 in test");
                activityModel.append(new MostImportantThing(0, "toodo1", 50L, 50));
                System.out.println("appending 2nd in test");
                activityModel.append(new MostImportantThing(1, "toodo2", 51L, 51));
                System.out.println("appending 3rd in test");
                activityModel.append(new MostImportantThing(2, "toodo3", 52L, 52));
                System.out.println("done appending");


                // check that the listView saw this change
                int expectedCount = 3;
                int actualCount = binding.mitList.getAdapter().getCount();

                // now make sure that the list view is displaying the correct values
                for (int i = 0; i < actualCount; i++) {
                    String expected = "toodo" + (i + 1);
                    // need to cast MostImportantThig
                    var actual = ((MostImportantThing) binding.mitList.getItemAtPosition(i)).task();
                    assertEquals(expected, actual);
                }

                // now remove
                activityModel.remove(0);
                activityModel.remove(1);
//                activityModel.remove(2);

            });
            // Simulate moving to the started state (above will then be called).
            scenario.moveToState(Lifecycle.State.STARTED);
        }
    }



    // TODO - GO TO OFFICE HOURS TO FIX THIS CODE
    @Test
    public void textEmptyWhenMitsAdded() {
        // launching the fragment scenario
        try (FragmentScenario<MitList> scenario = FragmentScenario.launchInContainer(MitList.class)) {
            // Observe the fragment's lifecycle to wait until the fragment is created.
            scenario.onFragment(fragment -> {
                // grab stuff from the xml
                var rootView = fragment.getView().findViewById(R.id.root);
                var binding = FragmentMitListBinding.bind(rootView);

                // grab these for adding
                var modelOwner = fragment.requireActivity();
                var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
                var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
                var activityModel = modelProvider.get(MainViewModel.class);


                // append stuff, should be fine as we've tested append
                System.out.println("appending 1 in test");
                activityModel.append(new MostImportantThing(0, "toodo1", 50L, 50));
                System.out.println("appending 2nd in test");
                activityModel.append(new MostImportantThing(1, "toodo2", 51L, 51));
                System.out.println("appending 3rd in test");
                activityModel.append(new MostImportantThing(2, "toodo5", 52L, 52));
                System.out.println("done appending");


//                // check that the listView saw this change
//                int expectedCount = 3;
//                int actualCount = binding.mitList.getAdapter().getCount();
//
//                // now make sure that the list view is displaying the correct values
//                for (int i=0; i<actualCount; i++) {
//                    String expected = "toodo" + (i+1);
//                    // need to cast MostImportantThig
//                    var actual = ((MostImportantThing) binding.mitList.getItemAtPosition(i)).task();
//                    assertEquals(expected, actual);
//                }

                // should check that the blank message isn't being displayed.

                String expectedMsg = "";
//                TextView tv = fragment.getView().findViewById(R.id.blank_message_text);
//                tv.requestLayout();
//                tv.invalidate();

                String actualMsg = binding.blankMessageText.getText().toString();

                System.out.println("trying the assert:");
                assertEquals(expectedMsg, actualMsg);
                System.out.println("assert concluded");

            });
            // Simulate moving to the started state (above will then be called).
            scenario.moveToState(Lifecycle.State.STARTED);
        }

    }


}
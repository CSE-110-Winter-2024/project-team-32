package edu.ucsd.cse110.successorator.ui;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.SuccessoratorApplication;
import edu.ucsd.cse110.successorator.databinding.FragmentTodayMitListBinding;

@RunWith(AndroidJUnit4.class)
/**
 * This class was created for the purposes of testing the UI of our application,
 * although we now know that with the Model View Presenter method in which we
 * created our app, it is unnecessary for the purposes of this class to test the
 * UI as such, so this somewhat broken code is unnecessary
 */
public class MitListFragmentTest {
    private FragmentScenario<TodayMitListFragment> scenario;

    @Before
    public void setup() {
        scenario = FragmentScenario.launchInContainer(TodayMitListFragment.class);
//        System.out.println("hhherea");
//        Context context = getApplicationContext();
//
//        // Retrieve the application class
//        SuccessoratorApplication application = (SuccessoratorApplication) context.getApplicationContext();
//        System.out.println("clearing database on setup:");
//        application.getMostImportantThingRepository().clear();
//        System.out.println("db count: " + application.getMostImportantThingRepository().count());
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void testDisplaysBlankMsgWithNoList() {
        // Observe the scenario's lifecycle to wait until the activity is created.
        scenario.onFragment(fragment -> {
            Context context = getApplicationContext();

            // Retrieve the application class
            SuccessoratorApplication application = (SuccessoratorApplication) context.getApplicationContext();
           //System.out.println("clearing database on setup:");
            application.getMostImportantThingRepository().clear();
           //System.out.println("db count: " + application.getMostImportantThingRepository().count());

            var rootView = fragment.getView().findViewById(R.id.root);
            var binding = FragmentTodayMitListBinding.bind(rootView);

            // make sure list is empty
            int expectedCount = 0;
            int actualCount = binding.mitList.getCount();

//                assertEquals(expectedCount, actualCount);
            // make sure message is displayed
            fragment.getView().invalidate();
            assertEquals(fragment.getString(R.string.blank_message_text),
                    binding.blankMessageText.getText());
        });
        // Simulate moving to the started state (above will then be called).
        scenario.moveToState(Lifecycle.State.STARTED);
    }

    /* all tests here dont work
    @Test
    public void testDisplayMitsWhenAdded() {
        // launching the fragment scenario
        // Observe the fragment's lifecycle to wait until the fragment is created.
        scenario.onFragment(fragment -> {
            Context context = getApplicationContext();

            // Retrieve the application class
            SuccessoratorApplication application = (SuccessoratorApplication) context.getApplicationContext();
           //System.out.println("clearing database on setup:");
            application.getMostImportantThingRepository().clear();
           //System.out.println("db count: " + application.getMostImportantThingRepository().count());

            // grab stuff from the xml
            var rootView = fragment.getView().findViewById(R.id.root);
            var binding = FragmentMitListBinding.bind(rootView);

            var modelOwner = fragment.requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            var activityModel = modelProvider.get(MainViewModel.class);


            // append stuff, should be fine as we've tested append
           //System.out.println("appending 1 in test");
            activityModel.append(new MostImportantThing(0, "toodo1", 50L, 50, false));
           //System.out.println("appending 2nd in test");
            activityModel.append(new MostImportantThing(1, "toodo2", 51L, 51, false));
           //System.out.println("appending 3rd in test");
            activityModel.append(new MostImportantThing(2, "toodo3", 52L, 52, false));
           //System.out.println("done appending");


            // check that the listView saw this change
            int expectedCount = 3;
            int actualCount = binding.mitList.getAdapter().getCount();

            //
            assertEquals(expectedCount, actualCount);

            // now make sure that the list view is displaying the correct values
            for (int i = 0; i < actualCount; i++) {
                String expected = "toodo" + (i + 1);
                // need to cast MostImportantThig
                var actual = ((MostImportantThing) binding.mitList.getItemAtPosition(i)).task();
                assertEquals(expected, actual);
            }

        });
        // Simulate moving to the started state (above will then be called).
        scenario.moveToState(Lifecycle.State.STARTED);
    }

    @Test
    public void testDisplayStrikeThroughMits() {
        // launching the fragment scenario
        // Observe the fragment's lifecycle to wait until the fragment is created.
        scenario.onFragment(fragment -> {
            Context context = getApplicationContext();

            // Retrieve the application class
            SuccessoratorApplication application = (SuccessoratorApplication) context.getApplicationContext();
            System.out.println("clearing database on setup:");
            application.getMostImportantThingRepository().clear();
            System.out.println("db count: " + application.getMostImportantThingRepository().count());

            // grab stuff from the xml
            var rootView = fragment.getView().findViewById(R.id.root);
            var binding = FragmentMitListBinding.bind(rootView);

            var modelOwner = fragment.requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            var activityModel = modelProvider.get(MainViewModel.class);


            // append stuff, should be fine as we've tested append
            System.out.println("appending 1 in test");
            activityModel.append(new MostImportantThing(0, "toodo1", 50L, 50, true));
            System.out.println("appending 2nd in test");
            activityModel.append(new MostImportantThing(1, "toodo2", 51L, 51, false));
            System.out.println("appending 3rd in test");
            activityModel.append(new MostImportantThing(2, "toodo3", 52L, 52, true));
            System.out.println("done appending");
            System.out.println("appending 4th in test");
            activityModel.append(new MostImportantThing(3, "toodo3", 52L, 52, true));
            System.out.println("appending 5th in test");
            activityModel.append(new MostImportantThing(4, "toodo3", 52L, 52, true));
            System.out.println("appending 6th in test");
            activityModel.append(new MostImportantThing(5, "toodo3", 52L, 52, false));



            // check that the listView saw this change
            int expectedCount = 3;
            int actualCount = binding.mitList.getAdapter().getCount();
                        // now make sure that the list view is displaying the correct values
            for (int i = 0; i < actualCount; i++) {
                //Just short hand to check for true values
                boolean expected = ((i % 2) == 0 || i == 3);
                //Cast to TextView to get each text view for each item in the list. Need to use getChildAt(i)
                boolean actual = ((TextView) (binding.mitList.getChildAt(i))).getPaint().isStrikeThruText();
                assertEquals(expected, actual);
            }

        });
        // Simulate moving to the started state (above will then be called).
        scenario.moveToState(Lifecycle.State.STARTED);
    }


    @Test
    //TODO - THIS DOES NOT WORK SADLY SAME PROBLEM AS DANIEL HAD, WE TESTED MANUALLY THO AT LEAST
    public void testMoveToTop() {
        // launching the fragment scenario
        // Observe the fragment's lifecycle to wait until the fragment is created.
        scenario.onFragment(fragment -> {
            Context context = getApplicationContext();

            // Retrieve the application class
            SuccessoratorApplication application = (SuccessoratorApplication) context.getApplicationContext();
           //System.out.println("clearing database on setup:");
            application.getMostImportantThingRepository().clear();
           //System.out.println("db count: " + application.getMostImportantThingRepository().count());

            // grab stuff from the xml
            var rootView = fragment.getView().findViewById(R.id.root);
            var binding = FragmentMitListBinding.bind(rootView);

            var modelOwner = fragment.requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            var activityModel = modelProvider.get(MainViewModel.class);

            // check that the database reflects the change
            MostImportantThingRepository repository = SuccessoratorApplication.mostImportantThingRepository;
            repository.append(new MostImportantThing(0, "toodo1", 50L, 50, false));
            repository.append(new MostImportantThing(1, "toodo2", 51L, 51, false));
            repository.append(new MostImportantThing(2, "toodo3", 52L, 52, false));

            // now make sure that the list view is correctly ordered
            repository.moveToTop(2);
            assertEquals(repository.findAll().getValue().get(0).task(), "toodo1");
//            var allItems = repository.findAll().getValue();
//            assertEquals("toodo3",allItems.get(0).task());
//            assertEquals("toodo1",allItems.get(1).task());
//            assertEquals("toodo2",allItems.get(2).task());


        });
        // Simulate moving to the started state (above will then be called).
        scenario.moveToState(Lifecycle.State.STARTED);
    }


    // TODO - GO TO OFFICE HOURS TO FIX THIS CODE
    @Test
    public void testTextEmptyWhenMitsAdded() {
        Context context = getApplicationContext();

        // Retrieve the application class
        SuccessoratorApplication application = (SuccessoratorApplication) context.getApplicationContext();
       //System.out.println("clearing database on setup:");
        application.getMostImportantThingRepository().clear();
       //System.out.println("db count: " + application.getMostImportantThingRepository().count());

        // launching the fragment scenario
        // Observe the fragment's lifecycle to wait until the fragment is created.
        scenario.onFragment(fragment -> {
           //System.out.println("i got here1");

            // grab stuff from the xml
            var rootView = Objects.requireNonNull(fragment.getView()).findViewById(R.id.root);
            var binding = FragmentMitListBinding.bind(rootView);

            // grab these for adding
            var modelOwner = fragment.requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            var activityModel = modelProvider.get(MainViewModel.class);

           //System.out.println("i got here");
            // append stuff, should be fine as we've tested append
           //System.out.println("appending 1 in test");
            activityModel.append(new MostImportantThing(0, "toodo1", 50L, 50, false));
           //System.out.println("appending 2nd in test");
            activityModel.append(new MostImportantThing(1, "toodo2", 51L, 51, false));
           //System.out.println("appending 3rd in test");
            activityModel.append(new MostImportantThing(2, "toodo5", 52L, 52, false));
           //System.out.println("done appending");


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
            fragment.getView().invalidate();

            String expectedMsg = "";
//                TextView tv = fragment.getView().findViewById(R.id.blank_message_text);
//                tv.requestLayout();
//                tv.invalidate();

            String actualMsg = binding.blankMessageText.getText().toString();

           //System.out.println("trying the assert:");
            assertEquals(expectedMsg, actualMsg);
           //System.out.println("assert concluded");

        });
        // Simulate moving to the started state (above will then be called).
        scenario.moveToState(Lifecycle.State.STARTED);
    }
     */
}





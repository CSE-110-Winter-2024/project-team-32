package edu.ucsd.cse110.successorator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.successorator.data.db.RoomMostImportantThingRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTimeKeeper;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.ui.PendingMitListFragment;
import edu.ucsd.cse110.successorator.ui.RecurringMitListFragment;
import edu.ucsd.cse110.successorator.ui.TodayMitListFragment;
import edu.ucsd.cse110.successorator.ui.TomorrowMitListFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreateMitDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreatePendingMitDialogFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreateRecurringMitDialogFragment;

/**
 * The MainActivity class of Succesorator that displays the user interface and
 * handles user interactions
 */
public class MainActivity extends AppCompatActivity {
    private static final int TODAY_VIEW = 0;
    private static final int TOMORROW_VIEW = 1;
    private static final int PENDING_VIEW = 2;
    private static final int RECURRING_VIEW = 3;
    private static int frag = 0;
    private ActivityMainBinding view;
    private TimeKeeper timeKeeper;
    private RoomMostImportantThingRepository roomMostImportantThings;
    private SuccessoratorDatabase db;
    private TextView dateTextView;
    private int incrementDateBy = 0;
    private MainViewModel activityModel;
    private int currentView;
    //NEW VARIABLE - represents the 'current date' of the app, which is sort
    //of the mocked date. When you press the advance day button, this updates,
    //The today and tomorrow fragment use this to calculate what should be shown
    //When you reload the app, it gets reset to the true current day
    private Date currDate;

    /**
     * Swaps from the current fragment to whatever fragment you pass in as an argument,
     * according for static variables above
     *
     * @param newFragment
     */
    private void swapFragments(int newFragment) {
        System.out.println("Swapping fragments from " + currentView + " to " + newFragment);

        switch (newFragment) {
            case TODAY_VIEW:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, TodayMitListFragment.newInstance(currDate))
                        .commit();
                this.currentView = TODAY_VIEW;
                break;
            case TOMORROW_VIEW:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, TomorrowMitListFragment.newInstance(currDate))
                        .commit();
                this.currentView = TOMORROW_VIEW;
                break;
            case PENDING_VIEW:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, PendingMitListFragment.newInstance(getSupportFragmentManager()))
                        .commit();
                this.currentView = PENDING_VIEW;
                break;
            case RECURRING_VIEW:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, RecurringMitListFragment.newInstance())
                        .commit();
                this.currentView = RECURRING_VIEW;
                break;
            default:
                throw new IllegalArgumentException("Trying to switch to a non-existing state");
        }
    }

    /**
     * Called when an activity is first created
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        this.currentView = TODAY_VIEW;
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());//, null, false);
        dateTextView = findViewById(R.id.action_bar_menu_advance_date);

        setContentView(view.getRoot());

//        scheduleAlarm(); // keeping for milestone 2, not needed now
        this.timeKeeper = new SimpleTimeKeeper(); // Initialize with current time
        this.timeKeeper.setDateTime(LocalDateTime.now());
        this.roomMostImportantThings = (RoomMostImportantThingRepository) SuccessoratorApplication.mostImportantThingRepository;
        //updates/adds recurring mits
        roomMostImportantThings.updateRecurringMits();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalDateTime lastTime = timeKeeper.getDateTime();
        LocalDateTime currentTime = LocalDateTime.now();

        System.out.println("Resumed the App!");
        dateTextView = findViewById(R.id.action_bar_menu_date);
        if (dateTextView != null) {
            Calendar c = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MM/dd");
            String date = dateFormat.format(c.getTime());
            dateTextView.setText(date);
            incrementDateBy = 0;
        }
        //When you resume, swap the fragment to the current fragment, just to
        //update the list in case the time has changed
        this.currDate = new Date();
        swapFragments(currentView);

        if (lastTime != null) {
            LocalDateTime twoAmToday = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.of(2, 0));
            if (lastTime.isBefore(twoAmToday) && currentTime.isAfter(twoAmToday)) {
                new Thread(() -> roomMostImportantThings.removeCompletedTasks(twoAmToday)).start();
            }
        }
        //updates/adds recurring mits
        roomMostImportantThings.updateRecurringMits();
        swapFragments(currentView);
    }

    /**
     * Initializes contents of the menu
     *
     * @param menu The options menu in which you place your items.
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        //When you make the options menu, add the date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MM/dd");
        String date = dateFormat.format(c.getTime());
        var dateItem = (menu.findItem(R.id.action_bar_menu_date)).setTitle(date);

        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        var itemId = item.getItemId();

        if (itemId == R.id.action_bar_menu_advance_date) {
            incrementDateBy++;
            advanceDate(incrementDateBy);
            roomMostImportantThings.setCurrDate(currDate);
            roomMostImportantThings.updateRecurringMits();
        }
        if (itemId == R.id.action_bar_menu_add_mit) {
            if (currentView == RECURRING_VIEW) {
                var dialogFragment = CreateRecurringMitDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(), "CreateRecurringMitDialogFragment");
                swapFragments(currentView);
            } else if (currentView == PENDING_VIEW) {
                var dialogFragment = CreatePendingMitDialogFragment.newInstance(currentView);
                dialogFragment.show(getSupportFragmentManager(), "CreatePendingMitDialogFragment");
                swapFragments(currentView);
            }
            else {
                var dialogFragment = CreateMitDialogFragment.newInstance(currentView);
                dialogFragment.show(getSupportFragmentManager(), "CreateMitDialogFragment");
                swapFragments(currentView);
            }
        } else if (itemId == R.id.go_to_today_view_button) {
            System.out.println("Trying to go to Today view");
            swapFragments(TODAY_VIEW);
        } else if (itemId == R.id.go_to_tomorrow_view_button) {
            System.out.println("Trying to go to Tomorrow view");
            swapFragments(TOMORROW_VIEW);
        } else if (itemId == R.id.go_to_pending_view_button) {
            System.out.println("Trying to go to Pending view");
            swapFragments(PENDING_VIEW);
        } else if (itemId == R.id.go_to_recurring_view_button) {
            System.out.println("Trying to go to Recurring view");
            swapFragments(RECURRING_VIEW);
        }

        return super.onOptionsItemSelected(item);
    }
    //only advances the date by 1 day
    //restarting the app will reset to current day
    public void advanceDate(int incrementDateBy) {

        dateTextView = findViewById(R.id.action_bar_menu_date);
        Calendar c = Calendar.getInstance();
//        String dateForTesting = dateTextView.getText().toString();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MM/dd");
        c.add(Calendar.DAY_OF_YEAR, incrementDateBy);
        this.currDate = c.getTime();
        String date = dateFormat.format(c.getTime());
        dateTextView.setText(date);
        new Thread(() -> roomMostImportantThings
                .removeCompletedTasks(LocalDateTime.ofInstant(c.getTime().toInstant(), ZoneId.systemDefault())))
                .start();
        //Update the view, THIS IS WHAT WILL END UP SHIFTING TOMORROW TASKS TO TODAY
        swapFragments(this.currentView);
    }

}


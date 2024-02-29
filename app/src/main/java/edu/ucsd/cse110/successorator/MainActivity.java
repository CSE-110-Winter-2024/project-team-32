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

import edu.ucsd.cse110.successorator.data.db.RoomMostImportantThingRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTimeKeeper;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.ui.PendingMitListFragment;
import edu.ucsd.cse110.successorator.ui.RecurringMitListFragment;
import edu.ucsd.cse110.successorator.ui.TodayMitListFragment;
import edu.ucsd.cse110.successorator.ui.TomorrowMitListFragment;
import edu.ucsd.cse110.successorator.ui.dialog.CreateMitDialogFragment;

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

    /**
     * Swaps from the current fragment to whatever fragment you pass in as an argument,
     * according for static variables above
     * @param newFragment
     */
    private void swapFragments(int newFragment) {
        System.out.println("Swapping fragments from " + currentView + " to " + newFragment);
        switch (newFragment) {
            case TODAY_VIEW:
                getSupportFragmentManager()
                       .beginTransaction()
                       .replace(R.id.fragment_container, TodayMitListFragment.newInstance())
                       .commit();
                this.currentView = TODAY_VIEW;
                break;
            case TOMORROW_VIEW:
                getSupportFragmentManager()
                       .beginTransaction()
                       .replace(R.id.fragment_container, TomorrowMitListFragment.newInstance())
                       .commit();
                this.currentView = TOMORROW_VIEW;
                break;
            case PENDING_VIEW:
                getSupportFragmentManager()
                       .beginTransaction()
                       .replace(R.id.fragment_container, PendingMitListFragment.newInstance())
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalDateTime lastTime = timeKeeper.getDateTime();
        LocalDateTime currentTime = LocalDateTime.now();

//        TODO - update menu
        System.out.println("Resumed the App!");

        if (lastTime != null) {
            LocalDateTime twoAmToday = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.of(2, 0));
            if (lastTime.isBefore(twoAmToday) && currentTime.isAfter(twoAmToday)) {
                new Thread(() -> roomMostImportantThings.removeCompletedTasks(twoAmToday)).start();
            }
        }
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
        }
        if (itemId == R.id.action_bar_menu_add_mit) {
            //Todo, make button initiate a Dialog
            var dialogFragment = CreateMitDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "CreateMitDialogFragment");
        }
        //Depricated, before we made dropdown menu - For testing:
//        if (itemId == R.id.action_bar_menu_swap_views) {
//            swapFragments((++frag % 4));
//        }

        else if (itemId == R.id.go_to_today_view_button) {
            System.out.println("Trying to go to Today view");
            swapFragments(TODAY_VIEW);
        }

        else if (itemId == R.id.go_to_tomorrow_view_button) {
            System.out.println("Trying to go to Tomorrow view");
            swapFragments(TOMORROW_VIEW);
        }

        else if (itemId == R.id.go_to_pending_view_button) {
            System.out.println("Trying to go to Pending view");
            swapFragments(PENDING_VIEW);
        }

        else if (itemId == R.id.go_to_recurring_view_button) {
            System.out.println("Trying to go to Recurring view");
            swapFragments(RECURRING_VIEW);
        }

        return super.onOptionsItemSelected(item);
    }

//    private void showPopupMenu(View view) {
//        PopupMenu popupMenu = new PopupMenu(this, view);
//        popupMenu.getMenuInflater().inflate(R.menu.action_bar, popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                // Handle dropdown menu item clicks here
//                int menuItemId = item.getItemId();
//                if (menuItemId == R.id.go_to_today_view_button) {
//
//                    return true;
//                }
//                else if (menuItemId == R.id.go_to_tomorrow_view_button) {
//
//                    return true;
//                }
//                else if (menuItemId == R.id.go_to_pending_view_button) {
//
//                    return true;
//                }
//                else if (menuItemId == R.id.go_to_recurring_view_button) {
//
//                    return true;
//                }
//                return false;
//            }
//        });
//        popupMenu.show();
//    }

    //only advances the date by 1 day
    //restarting the app will reset to current day
    public void advanceDate(int incrementDateBy) {
        dateTextView = findViewById(R.id.action_bar_menu_date);
        Calendar c = Calendar.getInstance();
//        String dateForTesting = dateTextView.getText().toString();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MM/dd");
        c.add(Calendar.DAY_OF_YEAR, incrementDateBy);
        String date = dateFormat.format(c.getTime());
        dateTextView.setText(date);
//        System.out.println("Advancing date from " + dateForTesting + " to " + date);
//        System.out.println("Advancing: IncrementDateBy is " + incrementDateBy);
        new Thread(() -> roomMostImportantThings
                .removeCompletedTasks(LocalDateTime.ofInstant(c.getTime().toInstant(), ZoneId.systemDefault())))
                .start();
    }


//    private void scheduleAlarm() {
//        scheduleAlarm(System.currentTimeMillis(), 2, 0);
//    }
//
//    private void scheduleAlarm(Long systemTime, int hour, int minute) {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 200, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // Set the alarm to start at 2 am every day
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(systemTime);
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//
//        // Repeat the alarm every day
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//    }
}

package edu.ucsd.cse110.successorator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import edu.ucsd.cse110.successorator.ui.dialog.CreateMitDialogFragment;

/**
 * The MainActivity class of Succesorator that displays the user interface and
 * handles user interactions
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private TimeKeeper timeKeeper;
    private RoomMostImportantThingRepository roomMostImportantThings;
    private SuccessoratorDatabase db;
    private TextView dateTextView;
    private int incrementDateBy = 0;
    private MainViewModel activityModel;

    /**
     * Called when an activity is first created
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());//, null, false);
        dateTextView = findViewById(R.id.action_bar_menu_advance_date);

        //old code, commented out in github repo
        //setContentView(view.getRoot());
        //this.view = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(view.getRoot());

        setContentView(view.getRoot());

//        scheduleAlarm(); // keeping for milestone 2, not needed now
        this.timeKeeper = new SimpleTimeKeeper(); // Initialize with current time
        this.timeKeeper.setDateTime(LocalDateTime.now());
        // Initialize the Successorator Room database
        // WAS MISTAKE! ALREADY MADE
//        this.db = Room.databaseBuilder(getApplicationContext(), SuccessoratorDatabase.class, "successorator_database2").build();

        // Initialize RoomMostImportantThingRepository with the DAO from your database
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

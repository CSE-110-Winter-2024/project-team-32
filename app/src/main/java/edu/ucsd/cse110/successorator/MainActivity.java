package edu.ucsd.cse110.successorator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.time.LocalDateTime;
import java.time.LocalTime;

import edu.ucsd.cse110.successorator.data.db.RoomMostImportantThingRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTimeKeeper;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.ui.dialog.CreateMitDialogFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private TimeKeeper timeKeeper;
    private RoomMostImportantThingRepository roomMostImportantThings;
    private SuccessoratorDatabase db;


    private MainViewModel activityModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());//, null, false);
        setContentView(view.getRoot());

//        scheduleAlarm(); // keeping for milestone 2, not needed now
        this.timeKeeper = new SimpleTimeKeeper(); // Initialize with current time
        this.timeKeeper.setDateTime(LocalDateTime.now());
        // Initialize the Successorator Room database
        this.db = Room.databaseBuilder(getApplicationContext(),
                SuccessoratorDatabase.class, "successorator_database").build();

        // Initialize RoomMostImportantThingRepository with the DAO from your database
        this.roomMostImportantThings = new RoomMostImportantThingRepository(db.mostImportantThingDao());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalDateTime lastTime = timeKeeper.getDateTime();
        LocalDateTime currentTime = LocalDateTime.now();
        timeKeeper.setDateTime(currentTime);

        if (lastTime != null) {
            LocalDateTime twoAmToday = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.of(2, 0));
            if (lastTime.isBefore(twoAmToday) && currentTime.isAfter(twoAmToday)) {
                new Thread(() -> roomMostImportantThings.removeCompletedTasks(twoAmToday)).start();
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();

        if (itemId == R.id.aciton_bar_menu_add_mit) {
            //Todo, make button initiate a Dialog
            var dialogFragment = CreateMitDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "CreateMitDialogFragment");

        }
        return super.onOptionsItemSelected(item);
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

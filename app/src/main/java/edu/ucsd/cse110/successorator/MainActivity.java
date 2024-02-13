package edu.ucsd.cse110.successorator;



import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.dialog.CreateMitDialogFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;


    private MainViewModel activityModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());//, null, false);
        setContentView(view.getRoot());

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        //When you make the options menu, add the date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
        String date = dateFormat.format(c.getTime());
        String day;
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday:";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
            default:
                day = "Not a day (" + c.get(Calendar.DAY_OF_WEEK) + ")";
        }
        date = day + " " + date;


        var dateItem = (menu.findItem(R.id.action_bar_menu_date)).setTitle(date);

        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();

        if (itemId == R.id.action_bar_menu_add_mit) {
            //Todo, make button initiate a Dialog
            var dialogFragment = CreateMitDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "CreateMitDialogFragment");

        }

//        if (itemId == R.id.action_bar_menu_date) {
//
//        }

        return super.onOptionsItemSelected(item);
    }





}

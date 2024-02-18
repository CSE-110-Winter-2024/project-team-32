package edu.ucsd.cse110.successorator;



import android.os.Bundle;
import android.util.Log;
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
import java.util.Date;


import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.dialog.CreateMitDialogFragment;

/**
 * The MainActivity class of Succesorator that displays the user interface and
 * handles user interactions
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private TextView dateTextView;
    private int incrementDateBy = 0;

    private MainViewModel activityModel;


    /**
     * Called when an activity is first created
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
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


    }

    /**
     * Initializes contents of the menu
     * @param menu The options menu in which you place your items.
     *
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

    /**
     * When an item from the menu item is clicked
     * @param item The menu item that was selected.
     *
     * @return true if interaction was handled, else false
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        incrementDateBy++;
        var itemId = item.getItemId();

        if (itemId == R.id.action_bar_menu_advance_date) {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MM/dd");
        c.add(Calendar.DAY_OF_YEAR, incrementDateBy);
        String date = dateFormat.format(c.getTime());
        dateTextView.setText(date);
    }


}

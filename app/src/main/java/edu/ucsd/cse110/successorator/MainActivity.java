package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.dialog.CreateMitDialogFragment;

/**
 * The MainActivity class of Succesorator that displays the user interface and
 * handles user interactions
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
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

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
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
        return true;
    }

    /**
     * When an item from the menu item is clicked
     * @param item The menu item that was selected.
     *
     * @return true if interaction was handled, else false
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();

        //When the plus icon is selected, create a dialog to add a new MIT
        if (itemId == R.id.aciton_bar_menu_add_mit) {
            var dialogFragment = CreateMitDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(),
                    "CreateMitDialogFragment");

        }
        return super.onOptionsItemSelected(item);
    }

}

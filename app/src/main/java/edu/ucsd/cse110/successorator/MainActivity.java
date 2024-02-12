package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.MitListAdapter;
import edu.ucsd.cse110.successorator.ui.dialog.CreateMitDialogFragment;
import edu.ucsd.cse110.successorator.ui.MitList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;


    private MainViewModel activityModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());//, null, false);
        setContentView(view.getRoot());

        //REMOVED since refactored to make a fragment for the default view
        //init adapter
//        this.adapter = new MitListAdapter(this, List.of());
//
//        this.activityModel.getOrderedMits().observe(mits -> {
//            if (mits == null) {
//                System.out.println("MainActivity got null mits");
//                return;
//            }
//            adapter.clear();
//            adapter.addAll(new ArrayList<>(mits));
//            adapter.notifyDataSetChanged();
//
//            // this feels like it violates SRP
//            if (mits.size() == 0) {
//                this.view.blankMessageText.setText(this.getString(R.string.blank_message_text));
//            }
//            if (mits.size() != 0) {
//                this.view.blankMessageText.setText("");
//            }
//        });
//        this.view.mi
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





}

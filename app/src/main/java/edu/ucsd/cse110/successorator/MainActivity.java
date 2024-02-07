package edu.ucsd.cse110.successorator;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.MitListAdapter;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private MitListAdapter adapter;
    private MainViewModel activityModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        this.view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);

        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);


        // init adapter
        this.adapter = new MitListAdapter(this, List.of());

        // init view
        this.view = ActivityMainBinding.inflate(getLayoutInflater());

        this.activityModel.getOrderedMits().observe(mits -> {
            if (mits == null) {
                System.out.println("MainActivity got null mits");
                return;
            }
            adapter.clear();
            adapter.addAll(new ArrayList<>(mits));
            adapter.notifyDataSetChanged();

            // this feels like it violates SRP
            if (mits.size() == 0) {
                this.view.blankMessageText.setText(this.getString(R.string.blank_message_text));
            }
            if (mits.size() != 0) {
                this.view.blankMessageText.setText("");
            }
        });
        this.view.mitList.setAdapter(adapter);
        setContentView(view.getRoot());


    }


}

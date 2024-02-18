package edu.ucsd.cse110.successorator.ui;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ListItemMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

public class MitListAdapter extends ArrayAdapter<MostImportantThing> {
    Consumer<Integer> onToggleCompletedClick; // for the future when we want to delete mits
    Consumer<Integer> onDeleteClick;
    public MitListAdapter(Context context,
                          List<MostImportantThing> mits,
                          Consumer<Integer> onToggleCompletedClick,
                          Consumer<Integer> onDeleteClick
    ) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(mits));
        this.onToggleCompletedClick = onToggleCompletedClick;
        this.onDeleteClick = onDeleteClick;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the mit for this position.
        var mit = getItem(position);
        assert mit != null;



        System.out.println("GETVIEW WAS CALLED FOR ID " + mit.id() +  " which has completed value of " + mit.completed());

        // Check if a view is being reused...
        ListItemMitBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemMitBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemMitBinding.inflate(layoutInflater, parent, false);
        }
        //TextView date = binding.getRoot().findViewById(R.id.action_bar_menu_date);
        //System.out.println("DATE is + " + date.getText());

        //Delete button that was implemented on accident, but doesn't get in
        //the way
        binding.cardDeleteButton.setOnClickListener(v -> {
            var id = mit.id();
            assert id != null;
            onDeleteClick.accept(id);
        });

        binding.toggleCompletedButton.setOnClickListener(v -> {
            var id = mit.id();
            assert id != null;
            boolean completed = !mit.completed();
            //System.out.println("Found ID");
            onToggleCompletedClick.accept(id);
            var taskText = binding.mitTaskText;
            //This is the logic that changes the text to strikethrough if it's completed
            if (!completed) {
                //Set the text to not strikethrough
                taskText.setPaintFlags(taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            else {
                //Set the text to strikethrough
                taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        //Make sure the items are in the correct state when the app is loaded
        var taskText = binding.mitTaskText;
        var checkBox = binding.toggleCompletedButton;
        if (!mit.completed()) {
            //Set the text to not strikethrough
            taskText.setPaintFlags(taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            checkBox.setChecked(false);
        }
        else {
            //Set the text to strikethrough
            checkBox.setChecked(true);
            taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // Populate the view with the mit's data.
        binding.mitTaskText.setText(mit.task());

        return binding.getRoot();
    }

    // The below methods aren't strictly necessary, usually.
    // but a lab said we need them
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        var mit = getItem(position);
        assert mit != null;

        var id = mit.id();
        assert id != null;

        return id;
    }
}

package edu.ucsd.cse110.successorator.ui;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ListItemMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

public class MitListAdapter extends ArrayAdapter<MostImportantThing> {
    Consumer<Integer> onToggleCompletedClick; // for the future when we want to delete mits
    public MitListAdapter(Context context,
                          List<MostImportantThing> mits,
                          Consumer<Integer> onToggleCompletedClick
    ) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(mits));
        this.onToggleCompletedClick = onToggleCompletedClick;
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

        binding.toggleCompletedButton.setOnClickListener(v -> {
            //System.out.println("Finding ID");
            var id = mit.id();
            assert id != null;
            //System.out.println("Found ID");
            onToggleCompletedClick.accept(id);
            var taskText = binding.mitTaskText;
            //This is the logic that changes the text to strikethrough if it's completed
            //mit.setCompleted(true);
            System.out.println("Completed is " + mit.completed());
            if (mit.completed()) {
                System.out.println("striking the text for id:" + mit.id());
                taskText.setPaintFlags(taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            else {
                System.out.println("unstriking the text! for the mit with id " + mit.id());
                taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

        });

        var taskText = binding.mitTaskText;
        var checkBox = binding.toggleCompletedButton;
        if (!mit.completed()) {
            System.out.println("unstriking the text for id:" + mit.id());
            taskText.setPaintFlags(taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            checkBox.setChecked(false);
        }
        else {
            checkBox.setChecked(true);
            System.out.println("striking the text! for the mit with id " + mit.id());
            taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        // Populate the view with the mit's data.
        binding.mitTaskText.setText(mit.task());//mit.task());

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

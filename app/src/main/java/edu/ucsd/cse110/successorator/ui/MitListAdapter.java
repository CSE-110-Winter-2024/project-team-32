package edu.ucsd.cse110.successorator.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ListItemMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

public class MitListAdapter extends ArrayAdapter<MostImportantThing> {
    Consumer<Integer> onToggleCompletedClick; // for the future when we want to delete mits
    Consumer<Integer> onDeleteClick;
    Date currDate;

    /**
     * Constructor for the MitListAdapter.
     *
     * @param context The context in which the adapter is being used
     * @param mits The list of MostImportantThing items to display
     * @param onToggleCompletedClick Toggling completion function
     * @param onDeleteClick Deletion function
     */
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
        this.currDate = currDate;
    }

    /**
     * Gets the view at a position in the dataset
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
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

        //Get the background before it's erased by setBackgroundColor
        Drawable background = binding.contextDisplay.getBackground();
        ColorStateList colorStateList;

        switch (mit.workContext()) {
            case "Home":
                System.out.println("Setting color for home!");
                colorStateList = ColorStateList.valueOf(binding.getRoot().getResources().getColor(R.color.homeColor));
                binding.contextDisplay.setBackgroundTintList(colorStateList);
                binding.contextDisplay.setText("H");
                break;
            case "Work":
                System.out.println("Setting color for work!");
                colorStateList = ColorStateList.valueOf(binding.getRoot().getResources().getColor(R.color.workColor));
                binding.contextDisplay.setBackgroundTintList(colorStateList);
                binding.contextDisplay.setText("W");
                break;
            case "School":
                System.out.println("Setting color for school!");
                colorStateList = ColorStateList.valueOf(binding.getRoot().getResources().getColor(R.color.schoolColor));
                binding.contextDisplay.setBackgroundTintList(colorStateList);
                binding.contextDisplay.setText("S");
                break;
            case "Errands":
                System.out.println("Setting color for errands!");
                colorStateList = ColorStateList.valueOf(binding.getRoot().getResources().getColor(R.color.errandsColor));
                binding.contextDisplay.setBackgroundTintList(colorStateList);
                binding.contextDisplay.setText("E");
                break;
            default:
                throw new IllegalStateException("Invalid state for mit");
        }
        //Required to re-update the background to being circular
        binding.contextDisplay.setBackground(background);

        return binding.getRoot();
    }

    // The below methods aren't strictly necessary, usually.
    // but a lab said we need them

    /**
     * Determines if the IDs are stable over changes
     * @return True if they are stable, else false
     */

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * Gets the item ID for the given position within the adapter
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return
     */
    @Override
    public long getItemId(int position) {
        var mit = getItem(position);
        assert mit != null;

        var id = mit.id();
        assert id != null;

        return id;
    }
}

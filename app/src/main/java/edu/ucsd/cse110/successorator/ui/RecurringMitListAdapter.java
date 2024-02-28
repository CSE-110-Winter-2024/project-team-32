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
import edu.ucsd.cse110.successorator.databinding.ListItemPendingMitBinding;
import edu.ucsd.cse110.successorator.databinding.ListItemRecurringMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.PendingMostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.RecurringMostImportantThing;

public class RecurringMitListAdapter extends ArrayAdapter<RecurringMostImportantThing> {
    Consumer<Integer> onDeleteClick;

    /**
     * Constructor for the MitListAdapter.
     *
     * @param context The context in which the adapter is being used
     * @param recurringMits The list of PendingMostImportantThing items to display
     * @param onDeleteClick Deletion function
     */
    public RecurringMitListAdapter(Context context,
                                 List<RecurringMostImportantThing> recurringMits,
                                 Consumer<Integer> onDeleteClick
    ) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(recurringMits));
        this.onDeleteClick = onDeleteClick;
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
        var recurringMit = getItem(position);
        assert recurringMit != null;


        System.out.println("RECURRING GETVIEW WAS CALLED FOR ID " + recurringMit.id());

        // Check if a view is being reused...
        ListItemRecurringMitBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemRecurringMitBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemRecurringMitBinding.inflate(layoutInflater, parent, false);
        }
        //TextView date = binding.getRoot().findViewById(R.id.action_bar_menu_date);
        //System.out.println("DATE is + " + date.getText());

        //Delete button that was implemented on accident, but doesn't get in
        //the way
        binding.cardDeleteButton.setOnClickListener(v -> {
            var id = recurringMit.id();
            assert id != null;
            onDeleteClick.accept(id);
        });

        //Make sure the items are in the correct state when the app is loaded
        var taskText = binding.mitTaskText;
        // Populate the view with the mit's data.
        binding.mitTaskText.setText(recurringMit.mit.task());

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

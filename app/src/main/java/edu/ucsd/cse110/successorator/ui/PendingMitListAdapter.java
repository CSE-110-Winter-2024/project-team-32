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
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ListItemMitBinding;
import edu.ucsd.cse110.successorator.databinding.ListItemPendingMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.PendingMostImportantThing;
import edu.ucsd.cse110.successorator.ui.dialog.MovePendingMitDialogFragment;

public class PendingMitListAdapter extends ArrayAdapter<PendingMostImportantThing> {
    Consumer<Integer> onDeleteClick;
    private FragmentManager fragmentManager;


    /**
     * Constructor for the MitListAdapter.
     *
     * @param context The context in which the adapter is being used
     * @param pendingMits The list of PendingMostImportantThing items to display
     * @param onDeleteClick Deletion function
     */
    public PendingMitListAdapter(Context context,
                          List<PendingMostImportantThing> pendingMits,
                          Consumer<Integer> onDeleteClick,
                          FragmentManager fragmentManager
    ) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem() for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash!
        super(context, 0, new ArrayList<>(pendingMits));
        this.fragmentManager = fragmentManager;
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
        var pendingMit = getItem(position);
        assert pendingMit != null;

        System.out.println("PENDING GETVIEW WAS CALLED FOR ID " + pendingMit.id());

        // Check if a view is being reused...
        ListItemPendingMitBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemPendingMitBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemPendingMitBinding.inflate(layoutInflater, parent, false);
        }
        //TextView date = binding.getRoot().findViewById(R.id.action_bar_menu_date);
        //System.out.println("DATE is + " + date.getText());

        //Delete button that was implemented on accident, but doesn't get in
        //the way
        binding.cardDeleteButton.setOnClickListener(v -> {
            var id = pendingMit.id();
            assert id != null;
            onDeleteClick.accept(id);
        });

        // When the list item is long-pressed, initiate dialog to move pending mit
        binding.getRoot().setOnLongClickListener(v -> {
            System.out.println("FragmentManager Long click!");
            System.out.println("FragmentManager is " + this.fragmentManager);
            var dialogFragment = MovePendingMitDialogFragment.newInstance(pendingMit);
            dialogFragment.show(this.fragmentManager, "MovePendingMitDialogFragment");
            return true;
        });

        //Make sure the items are in the correct state when the app is loaded
        var taskText = binding.mitTaskText;
        // Populate the view with the mit's data.
        binding.mitTaskText.setText(pendingMit.mit.task());

        //Get the background before it's erased by setBackgroundColor
        Drawable background = binding.contextDisplay.getBackground();
        ColorStateList colorStateList;

        switch (pendingMit.mit.workContext()) {
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

package edu.ucsd.cse110.successorator.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import edu.ucsd.cse110.successorator.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.databinding.ListItemRecurringMitBinding;
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



        //Make sure the items are in the correct state when the app is loaded
        var taskText = binding.mitTaskText;

        //Formate the recurrence period into the proper string
        String recurText = formatRecurrencePeriod(recurringMit);

        // Populate the view with the mit's data.
        binding.mitTaskText.setText(recurringMit.mit.task());
        binding.mitRecurringDateText.setText(recurText);
        //Get the background before it's erased by setBackgroundColor
        Drawable background = binding.contextDisplay.getBackground();
        ColorStateList colorStateList;

        // activate an on click listener for the constraint layout, so we can
        // press and hold to delete
        binding.recurringConstraintLayout.setOnLongClickListener(v -> {
            var id = recurringMit.id();
            assert id != null;
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
            builder.setTitle("Delete Recurring Task");
            builder.setMessage("Are you sure you want to delete this recurring task?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onDeleteClick.accept(id);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return true;
        });

        switch (recurringMit.mit.workContext()) {
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
        binding.contextDisplay.setBackground(background);
        //Required to re-update the background to being circular
        binding.contextDisplay.setBackground(background);

        return binding.getRoot();
    }

    public static String formatRecurrencePeriod(RecurringMostImportantThing recurringMit) {
        String recurText = recurringMit.recurPeriod;
        if (recurringMit.recurPeriod.equals("Weekly")) {
            //Calculate the day of week the task was made on
            Date date = new Date(recurringMit.mit.timeCreated());
            String dayOfWeekString = convToDayOfWeek(date);
            //Use DayOfWeek to get the string for day instead of integer
            recurText = recurText + " on " + dayOfWeekString;
        }
        else if (recurringMit.recurPeriod.equals("Monthly")) {
            Date date = new Date(recurringMit.mit.timeCreated());
            String dayOfWeekString = convToDayOfWeek(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            //Calculate which occurrence of the day it is in the month
            //e.g. 1st Tuesday, 2nd Tuesday, 3rd Tuesday
            System.out.println("NOW: the day of the month is " + dayOfMonth);
            int occurrenceOfDay = ((dayOfMonth - 1)/ 7);
            //Convert from 0, 1, 2, to 1st, 2nd, 3rd
            String occurrenceOfDayString = convToNthOccurenceInMonth(occurrenceOfDay);

            recurText = recurText + " " + occurrenceOfDayString + " " + dayOfWeekString;

        }
        else if (recurringMit.recurPeriod.equals("Yearly")) {
            Date date = new Date(recurringMit.mit.timeCreated());
            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            String dateInYear = format.format(date);
            recurText = recurText + " on " + dateInYear;
        }
        return recurText;
    }


    /**
     * Helper method to convert a date to the day of week it represents
     * @param date the date
     * @return the day of the week that date falls on
     */
    public static String convToDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String dayOfWeekString;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayOfWeekString = "Sun";
                break;
            case Calendar.MONDAY:
                dayOfWeekString = "Mon";
                break;
            case Calendar.TUESDAY:
                dayOfWeekString = "Tue";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeekString = "Wed";
                break;
            case Calendar.THURSDAY:
                dayOfWeekString = "Thu";
                break;
            case Calendar.FRIDAY:
                dayOfWeekString = "Fri";
                break;
            case Calendar.SATURDAY:
                dayOfWeekString = "Sat";
                break;
            default:
                dayOfWeekString = "Invalid Day";
                break;
        }
        return dayOfWeekString;
    }

    /**
     * Takes in the integer representing the nth occurrence of 
     * @param occurrenceOfDay n
     * @return the translation of n into which occurence it is
     */
    public static String convToNthOccurenceInMonth(int occurrenceOfDay) {
        System.out.println("NOW: occurenceOfDay is " + occurrenceOfDay);
        String occurrenceOfDayString;
        switch (occurrenceOfDay) {
            case 0:
                occurrenceOfDayString = "1st";
                break;
            case 1:
                occurrenceOfDayString = "2nd";
                break;
            case 2:
                occurrenceOfDayString = "3rd";
                break;
            case 3:
                occurrenceOfDayString  = "4th";
                break;
            case 4:
                occurrenceOfDayString = "5th";
                break;
            default:
                occurrenceOfDayString = "Invalid occurrenceOfDay";
                break;
        }
        return occurrenceOfDayString;
    }

    /**
     * Determines if the IDs are stable over changes
     * @return True if they are stable, else false
     */


    // The below methods aren't strictly necessary, usually.
    // but a lab said we need them


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

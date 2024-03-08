package edu.ucsd.cse110.successorator.ui.dialog;

import static edu.ucsd.cse110.successorator.ui.RecurringMitListAdapter.convToDayOfWeek;
import static edu.ucsd.cse110.successorator.ui.RecurringMitListAdapter.convToNthOccurenceInMonth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.RecurringMostImportantThing;

/**
 * Dialog Fragment for when the user is adding an MIT
 */
public class CreateMitDialogFragment extends DialogFragment {
    private FragmentDialogCreateMitBinding view;
    private MainViewModel activityModel;

    private int currentView;
    /**
     * Creates a new CreateMitDialogFragment instance
     * @return new CreateMitDialogFragment instance
     */

    public static CreateMitDialogFragment newInstance(int currentView) {
        var fragment = new CreateMitDialogFragment();
        Bundle args = new Bundle();
        args.putInt("currentView", currentView);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates fragment/initialization
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentView = getArguments().getInt("currentView", 0); // Default to 0 (TODAY_VIEW)
        }
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    /**
     * Creates dialog displayed by fragment
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return Dialog instance that's displayed by the fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateMitBinding.inflate(getLayoutInflater());
        Date currTime = new Date(); // Default to current time for TODAY_VIEW
        if (currentView == 1) { // Corresponds to Tomorrow view
            currTime = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
        }
        String weeklyText = "Weekly " + formatReccurencePeriod("Weekly", currTime);
        this.view.weeklyRadioButton.setText(weeklyText);
        String monthlyText = "Monthly " + formatReccurencePeriod("Monthly", currTime);
        this.view.monthlyRadioButton.setText(monthlyText);
        String yearlyText = "Yearly " + formatReccurencePeriod("Yearly", currTime);
        this.view.yearlyRadioButton.setText(yearlyText);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Enter your new task!")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    /**
     * When a positive button is clicked: add new MIT placeholder
     * @param dialog dialog from click
     * @param which button that was clicked
     */

    public void onPositiveButtonClick(DialogInterface dialog, int which) {
        var mitText = view.mitEditText.getText().toString();
        long taskTime = System.currentTimeMillis(); // Default to current time for TODAY_VIEW

        var checkedButton = view.mitOptionsRadioGroup.getCheckedRadioButtonId();
        if (currentView == 1) { // Corresponds to TOMORROW_VIEW
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Add one day for tomorrow's tasks
            taskTime = calendar.getTimeInMillis();
        }
        var mit = new MostImportantThing(null, mitText,taskTime,-1, false, "Home");
        if (checkedButton == R.id.one_time_radio_button) {
            this.activityModel.addNewMostImportantThing(mit);
        }
        else if (checkedButton == R.id.daily_radio_button) {
            this.activityModel.addNewRecurringMostImportantThing(new RecurringMostImportantThing(mit, "Daily"));
        }
        else if (checkedButton == R.id.weekly_radio_button) {
            this.activityModel.addNewRecurringMostImportantThing(new RecurringMostImportantThing(mit, "Weekly"));
        }
        else if (checkedButton == R.id.monthly_radio_button) {
            this.activityModel.addNewRecurringMostImportantThing(new RecurringMostImportantThing(mit, "Monthly"));
        }
        else if (checkedButton == R.id.yearly_radio_button) {
            this.activityModel.addNewRecurringMostImportantThing(new RecurringMostImportantThing(mit, "Yearly"));
        }

        //System.out.println("Trying to append an item via the UI");
        activityModel.addNewMostImportantThing(mit);




        dialog.dismiss();
    }

    /**
     * When a negative button is clicked, cancel
     * @param dialog dialog from click
     * @param which button that was clicked
     */
    public void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    private String formatReccurencePeriod(String recurPeriod, Date date) {
        String recurText = "";
        if (recurPeriod.equals("Weekly")) {
            //Calculate the day of week the task was made on
            String dayOfWeekString = convToDayOfWeek(date);
            //Use DayOfWeek to get the string for day instead of integer
            recurText = recurText + " on " + dayOfWeekString;
        }
        else if (recurPeriod.equals("Monthly")) {
            String dayOfWeekString = convToDayOfWeek(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            //Calculate which occurrence of the day it is in the month
            //e.g. 1st Tuesday, 2nd Tuesday, 3rd Tuesday
            int occurrenceOfDay = (dayOfMonth / 7);
            //Convert from 0, 1, 2, to 1st, 2nd, 3rd
            String occurrenceOfDayString = convToNthOccurenceInMonth(occurrenceOfDay);

            recurText = recurText + " " + occurrenceOfDayString + " " + dayOfWeekString;

        }
        else if (recurPeriod.equals("Yearly")) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd");
            String dateInYear = format.format(date);
            recurText = recurText + " on " + dateInYear;
        }
        return recurText;
    }

}

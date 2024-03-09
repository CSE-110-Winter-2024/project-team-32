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
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreatePendingMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.PendingMostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.RecurringMostImportantThing;

/**
 * Dialog Fragment for when the user is adding an MIT
 */
public class CreatePendingMitDialogFragment extends DialogFragment {
    private FragmentDialogCreatePendingMitBinding view;
    private MainViewModel activityModel;

    private int currentView;
    /**
     * Creates a new CreateMitDialogFragment instance
     * @return new CreateMitDialogFragment instance
     */

    public static CreatePendingMitDialogFragment newInstance(int currentView) {
        var fragment = new CreatePendingMitDialogFragment();
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
        this.view = FragmentDialogCreatePendingMitBinding.inflate(getLayoutInflater());

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

        var checkedContextButton = view.mitContextOptionsRadioGroup.getCheckedRadioButtonId();
        String context = "Default";
        if (checkedContextButton == R.id.home_radio_button) {
            context = "Home";
        }
        else if (checkedContextButton == R.id.work_radio_button) {
            context = "Work";
        }
        else if (checkedContextButton == R.id.school_radio_button) {
            context = "School";
        }
        else if (checkedContextButton == R.id.errands_radio_button) {
            context = "Errands";
        }

        var mit = new MostImportantThing(null, mitText,taskTime,-1, false, context);
        activityModel.addNewPendingMostImportantThing(new PendingMostImportantThing(mit));

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

package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateRecurringMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.RecurringMostImportantThing;

public class CreateRecurringMitDialogFragment extends DialogFragment{
    private FragmentDialogCreateRecurringMitBinding view;
    private MainViewModel activityModel;
    private Date currDate;

    /**
     * Creates a new CreateMitDialogFragment instance
     * @return new CreateMitDialogFragment instance
     */
    public static CreateRecurringMitDialogFragment newInstance(Date currDate) {
        var fragment = new CreateRecurringMitDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setCurrDate(currDate);
        return fragment;
    }

    /**
     * Creates recurring mit fragment/initialization
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    /**
     * Creates recurring mit dialog displayed by fragment
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return Dialog instance that's displayed by the fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogCreateRecurringMitBinding.inflate(getLayoutInflater());
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
        var mitText = view.recurringMitEditText.getText().toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currDate);
        if (!view.recurringMitDateEditTextNumber.getText().toString().equals("")) {
            int mitDay = Integer.valueOf(view.recurringMitDateEditTextNumber.getText().toString());
            cal.set(Calendar.DAY_OF_MONTH, mitDay);
        }
        if (!view.recurringMitMonthEditTextNumber.getText().toString().equals("")) {
            int mitMonth = Integer.valueOf(view.recurringMitMonthEditTextNumber.getText().toString());
            cal.set(Calendar.MONTH, mitMonth - 1);
        }
        if (!view.recurringMitYearEditTextNumber.getText().toString().equals("")) {
            int mitYear = Integer.valueOf(view.recurringMitYearEditTextNumber.getText().toString());
            cal.set(Calendar.YEAR, mitYear);
        }

//        var mitMonth = Integer.valueOf(view.recurringMitMonthEditTextNumber.getText().toString());
//        var mitYear = Integer.valueOf(view.recurringMitYearEditTextNumber.getText().toString());

//        cal.set(mitYear, mitMonth - 1, mitDay);

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

        var mit = new MostImportantThing(null, mitText,cal.getTimeInMillis(),-1, false, context);

        //if want daily reminders
        if (view.recurringMitDailyRadiobutton.isChecked()) {
            var recurringMit = new RecurringMostImportantThing(mit, "Daily");
            activityModel.addNewRecurringMostImportantThing(recurringMit);
        }
        else if (view.recurringMitWeeklyRadiobutton.isChecked()) {
            var recurringMit = new RecurringMostImportantThing(mit, "Weekly");
            activityModel.addNewRecurringMostImportantThing(recurringMit);
        }
        else if (view.recurringMitMonthlyRadiobutton.isChecked()) {
            var recurringMit = new RecurringMostImportantThing(mit, "Monthly");
            activityModel.addNewRecurringMostImportantThing(recurringMit);
        }
        else if (view.recurringMitYearlyRadiobutton.isChecked()) {
            var recurringMit = new RecurringMostImportantThing(mit, "Yearly");
            activityModel.addNewRecurringMostImportantThing(recurringMit);
        }
        this.activityModel.getMostImportantThingRepository().updateRecurringMits();
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

    public void setCurrDate(Date currDate) {
        this.currDate = currDate;
    }
}

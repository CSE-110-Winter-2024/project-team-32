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

    /**
     * Creates a new CreateMitDialogFragment instance
     * @return new CreateMitDialogFragment instance
     */
    public static CreateRecurringMitDialogFragment newInstance() {
        var fragment = new CreateRecurringMitDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);

        int mitDay;
        int mitMonth;
        int mitYear;

        if (view.recurringMitDateEditTextNumber.getText().toString().equals("")) {
            mitDay = calendar.get(Calendar.DATE);
        }
        else {
            mitDay = Integer.parseInt(view.recurringMitDateEditTextNumber.getText().toString());
        }
        if (view.recurringMitMonthEditTextNumber.getText().toString().equals("")) {
            mitMonth = calendar.get(Calendar.MONTH) + 1;
        }
        else {
            mitMonth = Integer.parseInt(view.recurringMitMonthEditTextNumber.getText().toString());
        }
        if (view.recurringMitYearEditTextNumber.getText().toString().equals("")) {
            mitYear = calendar.get(Calendar.YEAR);
        }
        else {
            mitYear = Integer.parseInt(view.recurringMitYearEditTextNumber.getText().toString());
        }

        Calendar cal = Calendar.getInstance();
        cal.set(mitYear, mitMonth - 1, mitDay);

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
}

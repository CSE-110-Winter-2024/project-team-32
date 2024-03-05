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

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MITContext;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

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


        String mitContext = ""; //makes home the default
        if(view.contextButtonHome.isChecked()) {
            mitContext = "Home";
        } else if(view.contextButtonErrand.isChecked()) {
            mitContext = "Errand";
        } else if(view.contextButtonWork.isChecked()) {
            mitContext = "Work";
        } else if(view.contextButtonSchool.isChecked()) {
            mitContext = "School";
        }

        if (currentView == 1) { // Corresponds to TOMORROW_VIEW
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Add one day for tomorrow's tasks
            taskTime = calendar.getTimeInMillis();
        }

        var mit = new MostImportantThing(null, mitText,taskTime,-1, false, "Home");
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

}

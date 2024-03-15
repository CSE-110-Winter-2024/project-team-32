package edu.ucsd.cse110.successorator.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateMitBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogMovePendingMitBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.PendingMostImportantThing;

/**
 * Dialog Fragment for when the user is adding an MIT
 */
public class MovePendingMitDialogFragment extends DialogFragment {
    private FragmentDialogMovePendingMitBinding view;
    private MainViewModel activityModel;
    private PendingMostImportantThing pendingMit;

    /**
     * Creates a new CreateMitDialogFragment instance
     * @return new CreateMitDialogFragment instance
     */

    public static MovePendingMitDialogFragment newInstance(PendingMostImportantThing pendingMit) {
        var fragment = new MovePendingMitDialogFragment();
        Bundle args = new Bundle();
        fragment.pendingMit = pendingMit;
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
        this.view = FragmentDialogMovePendingMitBinding.inflate(getLayoutInflater());
        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit pending task?")
                .setView(view.getRoot())
                .setPositiveButton("Confirm", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    /**
     * When a positive button is clicked, move according to selection
     * @param dialog dialog from click
     * @param which button that was clicked
     */
    public void onPositiveButtonClick(DialogInterface dialog, int which) {
        if (view.moveToTodayRadioButton.isChecked()) {
            activityModel.moveToToday(this.pendingMit);
        }
        else if (view.moveToTomorrowRadioButton.isChecked()) {
            activityModel.moveToTomorrow(this.pendingMit);
        }
        else if (view.deleteRadioButton.isChecked()) {
            activityModel.remove(this.pendingMit.id());
        }
        else if (view.finishRadioButton.isChecked()) {
            activityModel.finishPending(this.pendingMit);
        }
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

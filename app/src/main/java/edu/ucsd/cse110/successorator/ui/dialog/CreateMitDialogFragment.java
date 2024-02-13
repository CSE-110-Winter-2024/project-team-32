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
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

public class CreateMitDialogFragment extends DialogFragment {
    private FragmentDialogCreateMitBinding view;
    private MainViewModel activityModel;

    CreateMitDialogFragment() {

    }

    public static CreateMitDialogFragment newInstance() {
        var fragment = new CreateMitDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

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

    public void onPositiveButtonClick(DialogInterface dialog, int which) {
        var mitText = view.mitEditText.getText().toString();

        var mit = new MostImportantThing(null, mitText,System.currentTimeMillis(),-1, false);

        //System.out.println("Trying to append an item via the UI");
        activityModel.addNewMostImportantThing(mit);

        dialog.dismiss();
    }

    public void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

}

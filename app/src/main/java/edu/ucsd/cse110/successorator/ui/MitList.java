package edu.ucsd.cse110.successorator.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentMitListBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MitList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MitList extends Fragment {
    private MainViewModel activityModel;
    private FragmentMitListBinding view;

    private MitListAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Mit_list.
     */
    public static MitList newInstance() {
        MitList fragment = new MitList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = FragmentMitListBinding.inflate(inflater, container, false);

        setUpMvp();

        return view.getRoot();
    }

    private void setUpMvp() {
        // init adapter
        this.adapter = new MitListAdapter(this.getContext(), List.of(),activityModel::toggleCompleted,activityModel::remove);

        //Observers that display the MITs, or the default message if there are no MITs
        this.activityModel.getOrderedMits().observe(mits -> {
            if (mits == null) {
                System.out.println("MainActivity got null mits");
                return;
            }
            adapter.clear();
            adapter.addAll(new ArrayList<>(mits));
            adapter.notifyDataSetChanged();

            //Display the default message if there are no MITs
            if (isAdded()) {
                if (adapter.getCount() == 0) {
                    this.view.blankMessageText.setText(this.getString(R.string.blank_message_text));
                }
                if (adapter.getCount() != 0) {
                    this.view.blankMessageText.setText("");
                }
            }

        });
        this.view.mitList.setAdapter(adapter);
    }

}
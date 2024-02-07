package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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



       public MitList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Mit_list.
     */
    // TODO: Rename and change types and number of parameters
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
        this.adapter = new MitListAdapter(this.getContext(), List.of());

        this.activityModel.getOrderedMits().observe(mits -> {
            if (mits == null) {
                System.out.println("MainActivity got null mits");
                return;
            }
            adapter.clear();
            adapter.addAll(new ArrayList<>(mits));
            adapter.notifyDataSetChanged();

            // this feels like it violates SRP
            if (mits.size() == 0) {
                this.view.blankMessageText.setText(this.getString(R.string.blank_message_text));
            }
            if (mits.size() != 0) {
                this.view.blankMessageText.setText("");
            }
        });
        this.view.mitList.setAdapter(adapter);

    }

}
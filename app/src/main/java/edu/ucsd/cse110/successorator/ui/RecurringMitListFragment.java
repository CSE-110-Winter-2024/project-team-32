package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentRecurringMitListBinding;
import edu.ucsd.cse110.successorator.lib.domain.RecurringMostImportantThing;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayMitListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecurringMitListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentRecurringMitListBinding view;
    private RecurringMitListAdapter adapter;
    private String contextFocus;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Mit_list.
     */
    public static RecurringMitListFragment newInstance(String contextFocus) {
        RecurringMitListFragment fragment = new RecurringMitListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setContextFocus(contextFocus);
        return fragment;
    }

    /**
     * Initial creation of a fragment (before onCreate())
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize viewModel
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    /**
     * Called so fragment can create it's user view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = FragmentRecurringMitListBinding.inflate(inflater, container, false);

        setUpMvp();

        return view.getRoot();
    }

    /**
     * Set up Model-View-Presenter for fragment
     */
    private void setUpMvp() {
        // init adapter
        this.adapter = new RecurringMitListAdapter(this.getContext(), List.of(),activityModel::remove);

        Subject<List<RecurringMostImportantThing>> mitsToObserve;
        if (this.contextFocus == null) {
            System.out.println("ContextFocus was null in recurring");
            mitsToObserve = this.activityModel.getOrderedRecurringMits();
        }
        else if (this.contextFocus.equals("Any")) {
            //Get all mits
            mitsToObserve = this.activityModel.getOrderedRecurringMits();
        }
        else {
            //Get Mits only of the specific context
            mitsToObserve = this.activityModel.getOrderedRecurringMits(this.contextFocus);
        }

        //Observers that display the MITs, or the default message if there are no MITs
        mitsToObserve.observe(recurringMits -> {
            if (recurringMits == null) {
                System.out.println("MainActivity got null recurringMits");
                return;
            }
            adapter.clear();
            adapter.addAll(new ArrayList<>(recurringMits));
            adapter.notifyDataSetChanged();

        });

        this.view.mitList.setAdapter(adapter);
    }

    public void setContextFocus(String contextFocus) { this.contextFocus = contextFocus; }


}
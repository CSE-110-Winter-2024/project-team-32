package edu.ucsd.cse110.successorator.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTomorrowMitListBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayMitListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TomorrowMitListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTomorrowMitListBinding view;
    private MitListAdapter adapter;
    private String contextFocus;
    private Date currDate;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Mit_list.
     */
    public static TomorrowMitListFragment newInstance(Date currDate,String contextFocus) {
        TomorrowMitListFragment fragment = new TomorrowMitListFragment();
        fragment.setDate(currDate);
        fragment.setContextFocus(contextFocus);
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        view = FragmentTomorrowMitListBinding.inflate(inflater, container, false);

        setUpMvp();

        return view.getRoot();
    }

    /**
     * Set up Model-View-Presenter for fragment
     */
    private void setUpMvp() {
        // init adapter
        this.adapter = new MitListAdapter(this.getContext(), List.of(),activityModel::toggleCompleted, activityModel::remove);

        //Observers that display the MITs, or the default message if there are no MITs
        this.activityModel.getOrderedMits().observe(mits -> {
            if (mits == null) {
                System.out.println("MainActivity got null mits tomorrow");
                return;
            }
            adapter.clear();
            //sort through MITs and only add the ones that are tomorrow
            List<MostImportantThing> mitsToAdd = new ArrayList<>();
            for (var mit : mits) {
                //Set cal's time to when the mit was created
                //Subtract a day in milliseconds - 86400000
                Date dateCreatedMinusOneDay = new Date(mit.timeCreated() - TimeUnit.DAYS.toMillis(1));
                Date refDate;
                if (currDate != null) {
                    refDate = this.currDate;
                }
                else {
                    refDate = new Date();
                }
                System.out.println("refDate for tomorrow " + refDate);
                Instant instant1 = dateCreatedMinusOneDay.toInstant()
                        .truncatedTo(ChronoUnit.DAYS);
                Instant instant2 = refDate.toInstant()
                        .truncatedTo(ChronoUnit.DAYS);
                if (instant1.equals(instant2)) {
                    //only add if of the correct context
                    if (this.contextFocus == null) {
                        mitsToAdd.add(mit);
                    }
                    else if (this.contextFocus.equals("Any") || this.contextFocus.equals(mit.workContext())) {
                        mitsToAdd.add(mit);
                    }
                }
            }
            adapter.addAll(mitsToAdd);
            adapter.notifyDataSetChanged();
        });

        this.view.mitList.setAdapter(adapter);
    }

    public void setDate(Date date) {
        this.currDate = date;
    }

    public void setContextFocus(String contextFocus) { this.contextFocus = contextFocus; }
}
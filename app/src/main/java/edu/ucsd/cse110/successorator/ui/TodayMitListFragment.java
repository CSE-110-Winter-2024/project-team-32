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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentTodayMitListBinding;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayMitListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayMitListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTodayMitListBinding view;
    private MitListAdapter adapter;
    private Date currDate;
    private String contextFocus;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Mit_list.
     */
    public static TodayMitListFragment newInstance(Date currDate, String contextFocus) {
        TodayMitListFragment fragment = new TodayMitListFragment();
        fragment.setDate(currDate);
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
        view = FragmentTodayMitListBinding.inflate(inflater, container, false);

        setUpMvp();

        return view.getRoot();
    }

    /**
     * Set up Model-View-Presenter for fragment
     */
    private void setUpMvp() {
        // init adapter
        this.adapter = new MitListAdapter(this.getContext(), List.of(), activityModel::toggleCompleted,activityModel::remove);

        //Observers that display the MITs, or the default message if there are no MITs
        this.activityModel.getOrderedMits().observe(mits -> {
            if (mits == null) {
                System.out.println("MainActivity got null mits");
                return;
            }
            adapter.clear();
            //sort through MITs and only add the ones that are tomorrow
            List<MostImportantThing> mitsToAdd = new ArrayList<>();
            for (var mit : mits) {
                //Set cal's time to when the mit was created
                Date dateCreatedMinusOneDay = new Date(mit.timeCreated());
                Date refDate = new Date();
                if (this.currDate != null) {
                    refDate = this.currDate;
                }
                System.out.println("refDate for Today" + refDate);
                Instant instant1 = dateCreatedMinusOneDay.toInstant()
                        .truncatedTo(ChronoUnit.DAYS);
                Instant instant2 = refDate.toInstant()
                        .truncatedTo(ChronoUnit.DAYS);
                //If it was created for any day before today, display it
                if (instant1.compareTo(instant2) <= 0) {
                    //Go through and make sure it's not a duplicate
                    boolean isDuplicate = false;
                    for (MostImportantThing currMit : mitsToAdd) {
                        if (mit.task().equals(currMit.task())
                            && mit.workContext().equals(currMit.workContext())) {
                            isDuplicate = true;
                            //Bug fix - the finished task takes priority over the
                            // unfinished one, that way when you advance the day
                            // it shows the finished task that was previously at tomorrow,
                            // it doesn't show an unfinished goal
                            //if completed, need to override duplicate
                            if (mit.completed()) {
                                //Manually remove previous duplicate that was previously added
                                mitsToAdd.remove(currMit);
                                this.activityModel.getMostImportantThingRepository().remove(currMit.id());
                                isDuplicate = false;
                            }
                            //else don't override previous added duplicate, just don't add this new one
                            else {
                                this.activityModel.getMostImportantThingRepository().remove(mit.id());
                            }
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        //only add if of the correct context
                        if (this.contextFocus == null) {
                            mitsToAdd.add(mit);
                        }
                        else if (this.contextFocus.equals("Any") || this.contextFocus.equals(mit.workContext())) {
                            mitsToAdd.add(mit);
                        }
                    }
                }
            }
            adapter.addAll(mitsToAdd);
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

    public void setDate(Date date) {
        this.currDate = date;
    }

    public void setContextFocus(String contextFocus) { this.contextFocus = contextFocus; }

}
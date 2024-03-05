package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThingRepository;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThings;
import edu.ucsd.cse110.successorator.lib.domain.PendingMostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.RecurringMostImportantThing;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.ui.MitListAdapter;

/**
 * ViewModel class for Succesorator
 * manages UI and data interaction for main functionalities
 */
public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final MostImportantThingRepository mostImportantThingRepository; //Repo containing all MITs
    private final SimpleSubject<List<MostImportantThing>> orderedMits;//List keeping track of MIT order
    private final SimpleSubject<List<PendingMostImportantThing>> orderedPendingMits;
    private final SimpleSubject<List<RecurringMostImportantThing>> orderedRecurringMits;
    private final SimpleSubject<String> displayedTask;
    private MitListAdapter adapter;

    /**
     * initializes MainViewModel with it's dependencies
     */
    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(MainViewModel.class, creationExtras -> {
        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
        assert app != null;
        return new MainViewModel(app.getMostImportantThingRepository());
    });

    /**
     * Creates/initializes subjects and orders pre existing list
     *
     * @param mostImportantThingRepository the repository containing all the MostImportantThings
     */
    public MainViewModel(MostImportantThingRepository mostImportantThingRepository) {
        this.mostImportantThingRepository = mostImportantThingRepository;

        // Create the observable subjects.
        this.orderedMits = new SimpleSubject<>();
        this.orderedPendingMits = new SimpleSubject<>();
        this.orderedRecurringMits = new SimpleSubject<>();
        this.displayedTask = new SimpleSubject<>();

        //FOR TESTING, just add one pending item - it'll only add one cause the id will just override every time :)
//        this.mostImportantThingRepository.append(new PendingMostImportantThing(new MostImportantThing(100,"Pending Test MIT",System.currentTimeMillis(),-1,false,"Home")));
//        //FOR TESTING, just add one recurring item - it'll only add one cause the id will just overrid every time :)
//        this.mostImportantThingRepository.append(new RecurringMostImportantThing(new MostImportantThing(105,"Recurring Test Mit",System.currentTimeMillis(),-1,false,"Home"), "Yearly"));
//        //FOR TESTING, just add one item for tomorrow -it'll only add once cause the id will just override every time :)
//        this.mostImportantThingRepository.append(new MostImportantThing(110,"Tomorrow Test Mit",System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1),-1,false,"Home"));


        // When the list of mits changes (or is first loaded), reset the ordering.
        mostImportantThingRepository.findAllNormal().observe(mits -> {
            System.out.println("list of mits changed / got first loaded");
            if (mits == null) return; // not ready yet, ignore
            System.out.println("mits wasn't null, setting value now");
            var newOrderedMits = mits.stream() // begin streaming block
                    .sorted(Comparator.comparingInt(MostImportantThing::sortOrder)) // sorts it based on comparingSortOrder
                    .collect(Collectors.toList()); // ends the streaming block by producing a list
            this.orderedMits.setValue(newOrderedMits);
        });
        mostImportantThingRepository.findAllPending().observe(pendingMits -> {
            System.out.println("list of mits changed / got first loaded");
            if (pendingMits == null) return; // not ready yet, ignore
            System.out.println("mits wasn't null, setting value now");
            var newOrderedPendingMits = pendingMits.stream() // begin streaming block
                    //FOR RIGHT NOW, THEY ARE NOT SORTED SINCE THE INT WE WANT TO SORT IS INSIDE THE MIT INSTANCE VARIABLE INSIDE THE PENDINGMIT
                    //.sorted(Comparator.comparingInt(PendingMostImportantThing::mit.sortOrder)) // sorts it based on comparingSortOrder
                    .collect(Collectors.toList()); // ends the streaming block by producing a list
            this.orderedPendingMits.setValue(newOrderedPendingMits);
        });
        mostImportantThingRepository.findAllRecurring().observe(recurringMits -> {
            System.out.println("list of mits changed / got first loaded");
            if (recurringMits == null) return; // not ready yet, ignore
            System.out.println("mits wasn't null, setting value now");
            var newOrderedRecurringMits = recurringMits.stream() // begin streaming block
                    //FOR RIGHT NOW, THEY ARE NOT SORTED SINCE THE INT WE WANT TO SORT IS INSIDE THE MIT INSTANCE VARIABLE INSIDE THE RECURRINGMIT
                    //.sorted(Comparator.comparingInt(PendingMostImportantThing::mit.sortOrder)) // sorts it based on comparingSortOrder
                    .collect(Collectors.toList()); // ends the streaming block by producing a list
            this.orderedRecurringMits.setValue(newOrderedRecurringMits);
        });




    }

    /**
     * Retrieves the Subject tracking the current displayedTask
     *
     * @return the Subject tracking the current displayedTask
     */
    public Subject<String> getDisplayedTask() {
        return this.displayedTask;
    }

    /**
     * Retrieves the Subject tracking ordered list of MITs
     *
     * @return SimpleSubject<List < MostImportantThing>>; The Subject tracking ordered list of MITs
     */
    public Subject<List<MostImportantThing>> getOrderedMits() {
        return orderedMits;
    }

    public Subject<List<PendingMostImportantThing>> getOrderedPendingMits() {
        return this.orderedPendingMits;
    }

    public Subject<List<RecurringMostImportantThing>> getOrderedRecurringMits() {
        return this.orderedRecurringMits;
    }


    /**
     * Move the non Null MostImportantThing to the next position (up) and save
     */
    public void stepForward() {
        var mits = this.orderedMits.getValue();
        if (mits == null) return; // not ready yet

        var newMits = MostImportantThings.rotate(mits, -1);

        // update the repository with the new sort orders.
        mostImportantThingRepository.save(newMits);
    }


    /**
     * Move the non Null MIT backwards (1 space back) and save
     */
    public void stepBackward() {
        var mits = this.orderedMits.getValue();
        if (mits == null) return; // not ready yet

        // Build mapping from old to new sort orders using Math.floorMod to wrap around.
        var newMits = MostImportantThings.rotate(mits, 1);

        // update the repository with the new sort orders.
        mostImportantThingRepository.save(newMits);
    }

    /**
     * Insert MostImportantThing to the bottom of the repository
     *
     * @param mit The MostImportantThing to append
     */
    public void append(MostImportantThing mit) {
        mostImportantThingRepository.append(mit);
    }

    /**
     * Insert MostImportantThing to the top of the MITRepo
     *
     * @param mit The MostImportantThing to prepend
     */
    public void prepend(MostImportantThing mit) {
        mostImportantThingRepository.prepend(mit);
    }

    /**
     * Remove the MostImportantThing in the repository with value id
     *
     * @param id The ID of the MostImportantThing to be removed
     */
    public void remove(int id) {
        mostImportantThingRepository.remove(id);
    }

    /**
     * Mark the MostImportantThing with the value id as completed
     *
     * @param id The ID of the MostImportantThing to be marked completed
     */
    public void toggleCompleted(int id) {
        mostImportantThingRepository.toggleCompleted(id);
    }

    /**
     * Create and add a new MostImportantThing to repository
     *
     * @param mit The MostImportantThing to add
     */
    public void addNewMostImportantThing(MostImportantThing mit) {
        mostImportantThingRepository.addNewMostImportantThing(mit);
    }

    /**
     * Create and add a new recurring MostImportantThing to repository
     *
     * @param recurringMit The recurring MostImportantThing to add
     */
    public void addNewRecurringMostImportantThing(RecurringMostImportantThing recurringMit) {
        mostImportantThingRepository.addNewRecurringMostImportantThing(recurringMit);
    }

    public void removeCompletedTasks() {
        mostImportantThingRepository.removeCompletedTasks();
    }

    /**
     * clear all of the MostImportantThings from the repository
     */
    public void clear() {
        mostImportantThingRepository.clear();
    }

    public MostImportantThingRepository getMostImportantThingRepository() {
        return mostImportantThingRepository;
    }
}

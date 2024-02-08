package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThingRepository;
import edu.ucsd.cse110.successorator.lib.domain.MostImportantThings;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.ui.MitListAdapter;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final MostImportantThingRepository mostImportantThingRepository;
    private final SimpleSubject<List<MostImportantThing>> orderedMits;
    private final SimpleSubject<String> displayedTask;

    private MitListAdapter adapter;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getMostImportantThingRepository());
                    });

    public MainViewModel(MostImportantThingRepository mostImportantThingRepository) {
        this.mostImportantThingRepository = mostImportantThingRepository;

        // Create the observable subjects.
        this.orderedMits = new SimpleSubject<>();
        this.displayedTask = new SimpleSubject<>();

        // When the list of mits changes (or is first loaded), reset the ordering.
        mostImportantThingRepository.findAll().observe(mits -> {
            System.out.println("list of mits changed / got first loaded");
            if (mits == null) return; // not ready yet, ignore
            System.out.println("mits wasn't null, setting value now");
            var newOrderedMits = mits.stream() // begin streaming block
                    .sorted(Comparator.comparingInt(MostImportantThing::sortOrder)) // sorts it based on comparingSortOrder
                    .collect(Collectors.toList()); // ends the streaming block by producing a list
            this.orderedMits.setValue(newOrderedMits);

        });



    }

    public Subject<String> getDisplayedTask() {
        return this.displayedTask;
    }

    public Subject<List<MostImportantThing>> getOrderedMits() {
        return orderedMits;
    }


    public void stepForward() {
        var mits = this.orderedMits.getValue();
        if (mits == null) return; // not ready yet

        var newMits = MostImportantThings.rotate(mits, -1);

        // update the repository with the new sort orders.
        mostImportantThingRepository.save(newMits);
    }

    public void stepBackward() {
        var mits = this.orderedMits.getValue();
        if (mits == null) return; // not ready yet

        // Build mapping from old to new sort orders using Math.floorMod to wrap around.
        var newMits = MostImportantThings.rotate(mits, 1);

        // update the repository with the new sort orders.
        mostImportantThingRepository.save(newMits);
    }

    public void append(MostImportantThing mit) {
        mostImportantThingRepository.append(mit);
    }
    public void prepend(MostImportantThing mit) {
        mostImportantThingRepository.prepend(mit);
    }
    public void remove(int id) {
        mostImportantThingRepository.remove(id);
    }

    public void clear() {
        mostImportantThingRepository.clear();
    }
}

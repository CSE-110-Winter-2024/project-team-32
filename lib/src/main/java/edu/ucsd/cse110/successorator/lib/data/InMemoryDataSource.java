package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.MostImportantThing;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Class used as a sort of "database" of mostImportantThings that exist. I
 * implemented this for testing, as I don't know how to test sql databases
 */
public class InMemoryDataSource {
    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;

    private final Map<Integer, MostImportantThing> mostImportantThings
            = new HashMap<>();
    private final Map<Integer, MutableSubject<MostImportantThing>> mostImportantThingSubjects
            = new HashMap<>();
    private final MutableSubject<List<MostImportantThing>> allMostImportantThingsSubject
            = new SimpleSubject<>();

    public InMemoryDataSource() {
    }

    public final static List<MostImportantThing> TEST_MITS = List.of(
            new MostImportantThing(0, "todo1", 0L, 0, false, "Home"),
            new MostImportantThing(1, "todo2", 1L, 1, false, "Home"),
            new MostImportantThing(2, "todo3", 2L, 2, false, "Home"),
            new MostImportantThing(3, "todo4", 3L, 3, false, "Home"),
            new MostImportantThing(4, "todo5", 4L, 4, false, "Home"),
            new MostImportantThing(5, "todo6", 5L, 5, false, "Home")
    );

    public final static List<MostImportantThing> DEFAULT_MITS = List.of();
    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        data.putMostImportantThings(DEFAULT_MITS);
        return data;
    }

    public List<MostImportantThing> getMostImportantThings() {
        return List.copyOf(mostImportantThings.values());
    }

    public MostImportantThing getMostImportantThing(int id) {
        return mostImportantThings.get(id);
    }

    public Subject<MostImportantThing> getMostImportantThingSubject(int id) {
        if (!mostImportantThingSubjects.containsKey(id)) {
            var subject = new SimpleSubject<MostImportantThing>();
            subject.setValue(getMostImportantThing(id));
            mostImportantThingSubjects.put(id, subject);
        }
        return mostImportantThingSubjects.get(id);
    }

    public Subject<List<MostImportantThing>> getAllMostImportantThingsSubject() {
        return allMostImportantThingsSubject;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }

    public void putMostImportantThing(MostImportantThing mit) {
        var fixedCard = preInsert(mit);

        mostImportantThings.put(fixedCard.id(), fixedCard);
        postInsert();
        assertSortOrderConstraints();

        if (mostImportantThingSubjects.containsKey(fixedCard.id())) {
            mostImportantThingSubjects.get(fixedCard.id()).setValue(fixedCard);
        }
        allMostImportantThingsSubject.setValue(getMostImportantThings());
    }

    public void putMostImportantThings(List<MostImportantThing> mits) {
        var fixedCards = mits.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedCards.forEach(mit -> mostImportantThings.put(mit.id(), mit));
        postInsert();
        assertSortOrderConstraints();

        fixedCards.forEach(mit -> {
            if (mostImportantThingSubjects.containsKey(mit.id())) {
                mostImportantThingSubjects.get(mit.id()).setValue(mit);
            }
        });
        allMostImportantThingsSubject.setValue(getMostImportantThings());
    }

    public void removeMostImportantThing(int id) {
        var mit = mostImportantThings.get(id);
        var sortOrder = mit.sortOrder();

        mostImportantThings.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (mostImportantThingSubjects.containsKey(id)) {
            mostImportantThingSubjects.get(id).setValue(null);
        }
        allMostImportantThingsSubject.setValue(getMostImportantThings());
    }

    public void shiftSortOrders(int from, int to, int by) {
        var mits = mostImportantThings.values().stream()
                .filter(mit -> mit.sortOrder() >= from && mit.sortOrder() <= to)
                .map(mit -> mit.withSortOrder(mit.sortOrder() + by))
                .collect(Collectors.toList());

        putMostImportantThings(mits);
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * mits inserted have an id, and updates the nextId if necessary.
     */
    private MostImportantThing preInsert(MostImportantThing mit) {
        var id = mit.id();
        if (id == null) {
            // If the mit has no id, give it one.
            mit = mit.withId(nextId++);
        }
        else if (id > nextId) {
            // If the mit has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load mits like in fromDefault().
            nextId = id + 1;
        }

        return mit;
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that the
     * min and max sort orders are up to date after an insert.
     */
    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = mostImportantThings.values().stream()
                .map(MostImportantThing::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = mostImportantThings.values().stream()
                .map(MostImportantThing::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }

    /**
     * Safety checks to ensure the sort order constraints are maintained.
     * <p></p>
     * Will throw an AssertionError if any of the constraints are violated,
     * which should never happen. Mostly here to make sure I (Daniel) don't
     * write incorrect code by accident!
     */
    private void assertSortOrderConstraints() {
        // Get all the sort orders...
        var sortOrders = mostImportantThings.values().stream()
                .map(MostImportantThing::sortOrder)
                .collect(Collectors.toList());

        // Non-negative...
        assert sortOrders.stream().allMatch(i -> i >= 0);

        // Unique...
        assert sortOrders.size() == sortOrders.stream().distinct().count();

        // Between min and max...
        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
    }
}

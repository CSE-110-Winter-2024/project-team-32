package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Date;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Interface for MIT Repository
 * Used to implement the database, but it can also be
 * used to implement a simple InMemoryDatabase if we end up needing it
 *
 */
public interface MostImportantThingRepository {
    Subject<MostImportantThing> find(int id);

    Subject<List<MostImportantThing>> findAllNormal();

    Subject<List<PendingMostImportantThing>> findAllPending();
    Subject<List<PendingMostImportantThing>> findAllPending(String context);
    Subject<List<RecurringMostImportantThing>> findAllRecurring();
    Subject<List<RecurringMostImportantThing>> findAllRecurring(String context);

    Subject<List<MostImportantThing>> findAllOfContext(String context);

    void save(MostImportantThing mostImportantThing);

    void save(List<MostImportantThing> mostImportantThing);

    void remove(int id);

    void append(MostImportantThing mostImportantThing);
    void append(PendingMostImportantThing pendingMostImportantThing);
    void append(RecurringMostImportantThing recurringMostImportantThing);

    void prepend(MostImportantThing mostImportantThing);
    void prepend(PendingMostImportantThing pendingMostImportantThing);
    void prepend(RecurringMostImportantThing recurringMostImportantThing);

    void clear();

    void moveToTop(int id);

    void moveToTopOfFinished(int id);

    void addNewMostImportantThing(MostImportantThing mit);

    void addNewRecurringMostImportantThing(RecurringMostImportantThing recurringMit);

    void addNewPendingMostImportantThing(PendingMostImportantThing pendingMit);
    void updateRecurringMits();

    void toggleCompleted(int id);
    int count();

    void removeCompletedTasks();

    void setCurrDate(Date currDate);

    void moveToToday(PendingMostImportantThing pendingMit);

    void moveToTomorrow(PendingMostImportantThing pendingMit);

    void finishPending(PendingMostImportantThing pendingMit);
}

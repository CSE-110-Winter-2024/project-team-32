package edu.ucsd.cse110.successorator.lib.domain;

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

    Subject<List<MostImportantThing>> findAll();

    void save(MostImportantThing mostImportantThing);

    void save(List<MostImportantThing> mostImportantThing);

    void remove(int id);

    void append(MostImportantThing mostImportantThing);

    void prepend(MostImportantThing mostImportantThing);

    void clear();

    void toggleCompleted(int id);
    int count();
}
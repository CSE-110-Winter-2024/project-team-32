package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IMostImportantThing {
    @Nullable
    Integer id();

    @NonNull
    String task();

    @NonNull
    Long timeCreated();

    @NonNull
    Boolean completed();

    @NonNull
    String workContext();
    int sortOrder();

    MostImportantThing withId(int id);

    MostImportantThing withCompleted(boolean completed);

    MostImportantThing withSortOrder(int sortOrder);

    // TODO - this probably violates SRP
    void setCompleted(Boolean b);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}

package edu.ucsd.cse110.successorator.util;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Adapts live data into the subject interface
 * @param <T> the type of LiveData
 */
public class LiveDataSubjectAdapter<T> implements Subject<T> {
    private final LiveData<T> adapter;

    /**
     * Constructor given liveData
     * @param adapter the liveData to be adapted
     */
    public LiveDataSubjectAdapter(LiveData<T> adapter) {
        this.adapter = adapter;
    }

    /**
     * Retrieves current value held in liveData
     * @return the current value of liveData or null if no value
     */
    @Nullable
    @Override
    public T getValue() {
        return adapter.getValue();
    }

    /**
     * Assigns an observer to be updated by changes to liveData
     * @param observer The observer to add.
     */
    @Override
    public void observe(Observer<T> observer) {
        adapter.observeForever(observer::onChanged);
    }

    /**
     * Removes an observer from updates
     * @param observer The observer to remove.
     */
    @Override
    public void removeObserver(Observer<T> observer) {
        adapter.removeObserver(observer::onChanged);
    }
}

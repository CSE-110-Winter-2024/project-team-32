package edu.ucsd.cse110.successorator.util;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.lib.util.Subject;


public class LiveDataSubjectAdapter<T> implements Subject<T> {
    private final LiveData<T> adapter;

    public LiveDataSubjectAdapter(LiveData<T> adapter) {
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public T getValue() {
        return adapter.getValue();
    }

    @Override
    public void observe(Observer<T> observer) {
        adapter.observeForever(observer::onChanged);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        adapter.removeObserver(observer::onChanged);
    }
}

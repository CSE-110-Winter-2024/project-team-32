package edu.ucsd.cse110.successorator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create an instance of MainViewModel and call removeCompletedTasks
        MainViewModel viewModel = new ViewModelProvider((MainActivity) context).get(MainViewModel.class);
        viewModel.removeCompletedTasks();
        System.out.println("The method is called!");
    }
}

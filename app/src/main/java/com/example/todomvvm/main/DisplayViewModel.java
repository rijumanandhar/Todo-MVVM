package com.example.todomvvm.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskEntry;

import java.util.List;

public class DisplayViewModel extends AndroidViewModel {
    private static final String TAG = DisplayViewModel.class.getSimpleName();
    private LiveData<List<TaskEntry>> task;
    Repository repository;

    //constant to track which fragment is displayed
    public static boolean isDisplay = true;

    private MutableLiveData<Boolean> _showSnackBarEvent = new MutableLiveData<>();

    public LiveData<Boolean> showSnackBarEvent(){
        return  _showSnackBarEvent;
    }

    public DisplayViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG,"Actively retrieving the tasks from database");
        repository = new Repository(getApplication());
        task = repository.getAllTask();
    }

    public LiveData<List<TaskEntry>> getTask(){
        return task;
    }

    public void deleteTask(TaskEntry task){
        repository.delete(task);
        _showSnackBarEvent.setValue(true);
    }

}

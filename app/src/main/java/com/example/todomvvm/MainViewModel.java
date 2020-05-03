package com.example.todomvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todomvvm.database.AppDatabase;
import com.example.todomvvm.database.TaskEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<TaskEntry>> task;
    AppDatabase database;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG,"Actively retrieving the tasks from database");
        database = AppDatabase.getInstance(getApplication());
        task = database.taskDao().loadAllTask();
    }

    public LiveData<List<TaskEntry>> getTask(){
        return task;
    }

    public void deleteTask(final TaskEntry task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {

                database.taskDao().deleteTask(task);
            }
        });

    }

}

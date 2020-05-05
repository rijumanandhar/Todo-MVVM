package com.example.todomvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todomvvm.database.AppDatabase;
import com.example.todomvvm.database.TaskEntry;

public class AddEditTaskViewModel extends AndroidViewModel {

    private LiveData<TaskEntry> task;
    AppDatabase database;

    public LiveData<TaskEntry> getTask(){
        return task;
    }

    public AddEditTaskViewModel(@NonNull Application application, int id) {
        super(application);
        database = AppDatabase.getInstance(getApplication());
        task = database.taskDao().loadTaskById(id);
    }

    public AddEditTaskViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(getApplication());
    }

    public void addTask (final TaskEntry task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                database.taskDao().insertTask(task);
            }
        });

    }

    public void updateTask (final TaskEntry task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                database.taskDao().update(task);
            }
        });

    }
}

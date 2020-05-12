package com.example.todomvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todomvvm.database.AppDatabase;
import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskEntry;

public class AddEditTaskViewModel extends AndroidViewModel {

    private LiveData<TaskEntry> task;
    Repository repository;

    public LiveData<TaskEntry> getTask(){
        return task;
    }

    public AddEditTaskViewModel(@NonNull Application application, int id) {
        super(application);
        repository = new Repository(getApplication());
        task = repository.getTaskById(id);
    }

    public AddEditTaskViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(getApplication());
    }

    public void addTask (TaskEntry task){
        repository.insert(task);

    }

    public void updateTask (TaskEntry task){
        repository.update(task);
    }
}

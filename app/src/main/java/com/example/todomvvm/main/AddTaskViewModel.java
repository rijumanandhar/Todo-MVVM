package com.example.todomvvm.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskEntry;

public class AddTaskViewModel extends AndroidViewModel {
    private int priority = 1;
    Repository repository;

    public AddTaskViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(getApplication());
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void addTask (TaskEntry task){
        repository.insert(task);
    }
}

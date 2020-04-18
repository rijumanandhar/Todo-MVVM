package com.example.todomvvm.addedittask;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.todomvvm.database.Repository;

public class AddEditTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    Repository repository;
    int taskId;

    public AddEditTaskViewModelFactory(Repository repository, int taskId){
        this.repository = repository;
        this.taskId = taskId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return  (T) new AddEditTaskViewModel(repository, taskId);
    }
}

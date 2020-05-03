package com.example.todomvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddEditTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    Application application;
    int id;

    public AddEditTaskViewModelFactory(Application application, int id){
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddEditTaskViewModel(application, id);
    }
}

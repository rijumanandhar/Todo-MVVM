package com.example.todomvvm.edittask;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    Application application;
    int id;

    public EditTaskViewModelFactory(Application application, int id){
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditTaskViewModel(application, id);
    }
}

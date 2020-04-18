package com.example.todomvvm.tasks;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskEntry;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    Repository repository;

   private  LiveData<List<TaskEntry>> tasks;

    public  MainActivityViewModel(Repository repository){
        this.repository = repository;
        tasks = repository.getTasks();
    }

    public LiveData<List<TaskEntry>> getTasks(){
        return tasks;
    }

    public void deleteTask(TaskEntry task){
        repository.deleteTask(task);
    }

}

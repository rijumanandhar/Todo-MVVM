package com.example.todomvvm.addedittask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskEntry;

public class AddEditTaskViewModel extends ViewModel {

    Repository repository;
    LiveData<TaskEntry> task;

    AddEditTaskViewModel(Repository repository, int taskId){
        this.repository = repository;
        if(taskId != -1)
            task = repository.getTaskById(taskId);
    }


    public LiveData<TaskEntry> getTask(){
        return task;
    }

    public void insertTask(TaskEntry task){
        repository.insertTask(task);
    }

    public void updateTask(TaskEntry task){
        repository.insertTask(task);
    }


}

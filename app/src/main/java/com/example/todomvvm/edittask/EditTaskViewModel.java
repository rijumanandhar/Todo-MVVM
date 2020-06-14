package com.example.todomvvm.edittask;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todomvvm.database.Reminder;
import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskEntry;

public class EditTaskViewModel extends AndroidViewModel {
    // Constant for logging
    private static final String TAG = EditTaskViewModel.class.getSimpleName();

    //Keep track of rotation
    public static boolean userClick = false;

    public static int viewModelTaskId;

    private LiveData<TaskEntry> task;
    Repository repository;
    public static int mTaskId = EditTaskActivity.DEFAULT_TASK_ID;

    public LiveData<TaskEntry> getTask(){
        return task;
    }

    public EditTaskViewModel(@NonNull Application application, int id) {
        super(application);
        repository = new Repository(getApplication());
        task = repository.getTaskById(id);
        Log.d(TAG," Created");
    }

    public void updateTask (TaskEntry task){
        repository.updateTask(task);
    }

    public LiveData<Reminder> getReminder(int id){
        return repository.getReminderByTaskId(id);
    }
}

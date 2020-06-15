package com.example.todomvvm.edittask;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todomvvm.database.Reminder;
import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskEntry;

import java.util.Date;
import java.util.List;

public class EditTaskViewModel extends AndroidViewModel {
    // Constant for logging
    private static final String TAG = EditTaskViewModel.class.getSimpleName();

    //Keep track of rotation
    public static boolean userClick = false;

    public static int viewModelTaskId;
    private boolean isReminder = false;
    private Date updateDate;
    private int reminderId = 0;

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

    public EditTaskViewModel(Application application){
        super(application);
        repository = new Repository(getApplication());
    }

    public void updateTask (TaskEntry task){
        repository.updateTask(task);
    }

    public LiveData<List<TaskEntry>> getAllTasks(){
        return repository.getAllTask();
    }

    public LiveData<Reminder> getReminder(int id){
        return repository.getReminderByTaskId(id);
    }

    public boolean isReminder() {
        return isReminder;
    }
    public void setReminderBool(boolean reminder) {
        isReminder = reminder;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public void updateReminder(Reminder reminder){
        repository.updateReminder(reminder);
    }

    public void insertReminder(Reminder reminder){
        repository.insertReminder(reminder);
    }
}

package com.example.todomvvm.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todomvvm.database.Reminder;
import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskEntry;

import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private int priority = 1;
    private String description;
    private LiveData<List<TaskEntry>> task;
    private int maxTaskId = 0;
    Repository repository;
    private boolean isReminder = false;
    private Date remindDate;

    //constant to track which fragment is displayed
    public static boolean listDisplayFragment = true;

    private MutableLiveData<Boolean> _showSnackBarEvent = new MutableLiveData<>();

    public LiveData<Boolean> showSnackBarEvent(){
        return  _showSnackBarEvent;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG,"Actively retrieving the tasks from database");
        repository = new Repository(getApplication());
        task = repository.getAllTask();
    }

    public LiveData<Integer> getMaximumId() {
        return repository.getMaxTaskId();
    }

    public int getMaxTaskId() {
        return maxTaskId;
    }

    public void setMaxTaskId(int maxTaskId) {
        this.maxTaskId = maxTaskId;
    }

    public LiveData<List<TaskEntry>> getTask(){
        return task;
    }

    public void deleteTask(TaskEntry task){
        repository.deleteTask(task);
        _showSnackBarEvent.setValue(true);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addTask (TaskEntry task){
        repository.insertTask(task);
        _showSnackBarEvent.setValue(false);
    }

    public void addReminder (TaskEntry task, Reminder reminder){
        repository.insertReminder(task,reminder);
        _showSnackBarEvent.setValue(false);
    }

    public LiveData<Reminder> getReminderById(int taskId){
        return repository.getReminderByTaskId(taskId);
    }

    public void deleteReminder (Reminder reminder){
        repository.deleteReminder(reminder);
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }
}

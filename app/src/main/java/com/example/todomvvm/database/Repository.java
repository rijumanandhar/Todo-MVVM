package com.example.todomvvm.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {

    private TaskDao taskDao;

    public Repository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        taskDao = database.taskDao();
    }

    public LiveData<List<TaskEntry>> getAllTaskByPriority(){
        return taskDao.loadAllTaskByPriority();
    }

    public LiveData<List<TaskEntry>> getAllTaskByDate(){
        return taskDao.loadAllTaskByDate();
    }

    public LiveData<Integer> getMaxTaskId(){
        return taskDao.loadMaxTaskId();
    }

    public LiveData<TaskEntry> getTaskById(int id){
        return taskDao.loadTaskById(id);
    }

    public void insertTask(final TaskEntry task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.insertTask(task);
            }
        });
    }

    public void updateTask(final TaskEntry task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.updateTask(task);
            }
        });
    }

    public void deleteTask(final TaskEntry task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.deleteTask(task);
            }
        });
    }

    //Reminder

    //getReminder
    public LiveData<Reminder> getReminderByTaskId(int id){
        return taskDao.loadReminderByTaskId(id);
    }

    //insert
    public void insertReminder(final TaskEntry task, final Reminder reminder){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.insertReminderForTask(task,reminder);
            }
        });
    }

    public void insertReminder(final Reminder reminder){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.insertReminder(reminder);
            }
        });
    }

    //update
    public void updateReminder(final Reminder reminder){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.updateReminder(reminder);
            }
        });
    }

    //delete
    public void deleteReminder(final Reminder reminder){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.deleteReminder(reminder);
            }
        });
    }

    //reminderanduser
    public LiveData<List<TaskReminder>> getTaskAndReminder(){
        return taskDao.loadTaskAndReminder();
    }

}

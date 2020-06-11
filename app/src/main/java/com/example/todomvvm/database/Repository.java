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

    public LiveData<List<TaskEntry>> getAllTask(){
        return taskDao.loadAllTask();
    }

    public LiveData<Integer> getTaskCount(){
        return taskDao.countAllTask();
    }

    public LiveData<TaskEntry> getTaskById(int id){
        return taskDao.loadTaskById(id);
    }

    public void insert(final TaskEntry task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.insertTask(task);
            }
        });
    }

    public void update (final TaskEntry task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.update(task);
            }
        });
    }

    public void delete(final TaskEntry task){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.deleteTask(task);
            }
        });
    }
}

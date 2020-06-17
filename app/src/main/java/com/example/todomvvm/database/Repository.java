package com.example.todomvvm.database;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.todomvvm.main.NotificationAlertReceiver;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Repository {

    private TaskDao taskDao;
    private DatabaseReference dbUser;
    private DatabaseReference dbTask;
    private DatabaseReference dbReminder;
    private StorageReference userPicStorage;

    public Repository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        taskDao = database.taskDao();
    }

    public Repository(){
        dbUser = FirebaseDatabase.getInstance().getReference().child("user");
        dbTask = FirebaseDatabase.getInstance().getReference().child("task");
        dbReminder = FirebaseDatabase.getInstance().getReference().child("reminder");
        userPicStorage = FirebaseStorage.getInstance().getReference().child("user_profile");
    }
    public LiveData<List<TaskEntry>> getAllTask(){
        return taskDao.loadAllTask();
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

    public void insertUserData(String uid,String name, String email, String username, String picture){
        DatabaseReference mRef = dbUser.child(uid);
        mRef.child("name").setValue(name);
        mRef.child("username").setValue(username);
        mRef.child("email").setValue(email);
        mRef.child("picture").setValue(picture);
    }

    public void insertTaskDataToCloud(TaskEntry task, String uid){
        DatabaseReference mRef = dbTask.child(uid);
        mRef.child("id").setValue(task.getId());
        mRef.child("description").setValue(task.getDescription());
        mRef.child("updatedat").setValue(task.getUpdatedAt());
    }

    public void insertReminderDataToCloud(Reminder reminder, String uid){
        DatabaseReference mRef = dbReminder.child(uid);
        mRef.child("id").setValue(reminder.getReminderId());
        mRef.child("date").setValue(reminder.getRemindDate());
        mRef.child("taskid").setValue(reminder.getTaskId());
    }

}

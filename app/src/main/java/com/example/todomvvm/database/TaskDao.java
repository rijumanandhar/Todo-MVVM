package com.example.todomvvm.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class TaskDao {
    @Query("Select * from task order by priority")
    abstract LiveData<List<TaskEntry>> loadAllTask();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertTask(TaskEntry task);

    @Update
    abstract void updateTask(TaskEntry task);

    @Delete
    abstract void deleteTask(TaskEntry task);

    @Query("Select * from task where id = :id")
    abstract LiveData<TaskEntry> loadTaskById (int id);

//Reminder

    @Transaction
    @Query("SELECT * FROM task")
    abstract public LiveData<List<TaskReminder>> loadTaskAndReminder();

    @Query("Select * from Reminder where taskId = :taskId")
    abstract LiveData<Reminder> loadReminderByTaskId (int taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertReminder(Reminder reminder);

    @Update
    abstract void updateReminder(Reminder reminder);

    @Delete
    abstract void deleteReminder(Reminder reminder);

    public void insertReminderForTask(TaskEntry task, Reminder reminder){
        reminder.setTaskId(task.getId());
        Log.d("AddTask","Reminder in dao "+task.getId());
        insertReminder(reminder);
    }
}

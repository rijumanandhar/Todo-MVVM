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
    abstract LiveData<List<TaskEntry>> loadAllTaskByPriority();

    @Query("Select * from task order by updated_at desc")
    abstract LiveData<List<TaskEntry>> loadAllTaskByDate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertTask(TaskEntry task);

    @Update
    abstract void updateTask(TaskEntry task);

    @Delete
    abstract void deleteTask(TaskEntry task);

    @Query("Select * from task where id = :id")
    abstract LiveData<TaskEntry> loadTaskById (int id);

    @Query("Select count(*) from task")
    abstract LiveData<Integer> countAllTask();

    @Query("Select max(id) from task")
    abstract LiveData<Integer> loadMaxTaskId ();

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

    @Query("Delete from Reminder where reminderId = :id")
    abstract void deleteReminderById(int id);

    public void insertReminderForTask(TaskEntry task, Reminder reminder){
        reminder.setTaskId(task.getId());
        //Log.d("AddTask","Reminder in dao "+task.getId());
        insertReminder(reminder);
    }
}

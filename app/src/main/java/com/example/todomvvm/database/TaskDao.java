package com.example.todomvvm.database;

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
public interface TaskDao {

    @Query("Select * from task order by priority")
    LiveData<List<TaskEntry>> loadAllTask();

//    @Query("Select count(*) from task")
//    LiveData<Integer> countAllTask();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(TaskEntry task);

    @Update
    void updateTask(TaskEntry task);

    @Delete
    void deleteTask(TaskEntry task);

    @Query("Select * from task where id = :id")
    LiveData<TaskEntry> loadTaskById (int id);

    //Reminder

    @Transaction
    @Query("SELECT * FROM task")
    public LiveData<List<TaskReminder>> loadTaskAndReminder();

    @Query("Select * from Reminder where taskId = :taskId")
    LiveData<Reminder> loadReminderByTaskId (int taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminder(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);
}


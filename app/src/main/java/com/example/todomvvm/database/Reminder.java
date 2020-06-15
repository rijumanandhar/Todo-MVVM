package com.example.todomvvm.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Reminder")
public class Reminder {
   @PrimaryKey(autoGenerate = true)
   @NonNull
    private int reminderId;
    private Date remindDate;
    private int taskId;

    @Ignore
    public Reminder(int taskId, Date remindDate){
        this.taskId = taskId;
        this.remindDate = remindDate;
    }

    public Reminder(int reminderId, int taskId, Date remindDate){
        this.reminderId = reminderId;
        this.taskId = taskId;
        this.remindDate = remindDate;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}

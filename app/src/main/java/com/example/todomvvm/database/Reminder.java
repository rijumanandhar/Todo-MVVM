package com.example.todomvvm.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Reminder")
public class Reminder {
   @PrimaryKey
   @NonNull
    private int reminderId;
    private String status;
    private Date remindDate;
    private int taskId;

    @Ignore
    public Reminder(int taskId, Date remindDate, String status){
        this.taskId = taskId;
        this.remindDate = remindDate;
        this.status = status;
    }

    public Reminder(int reminderId, int taskId, Date remindDate, String status){
        this.reminderId = reminderId;
        this.taskId = taskId;
        this.remindDate = remindDate;
        this.status = status;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

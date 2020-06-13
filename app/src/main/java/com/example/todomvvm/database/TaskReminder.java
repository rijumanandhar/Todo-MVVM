package com.example.todomvvm.database;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TaskReminder {
    @Embedded public TaskEntry task;
    @Relation(
            parentColumn = "id",
            entityColumn = "taskId"
    )
    public Reminder reminder;
}

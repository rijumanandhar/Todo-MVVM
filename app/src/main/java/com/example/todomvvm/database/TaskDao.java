package com.example.todomvvm.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("select * from task order by priority")
    List<TaskEntry> getAllTodos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodo(TaskEntry task);

    @Delete
    void deleteTodo(TaskEntry task);

    @Update
    void update(TaskEntry task);

    @Query("select * from task where id = :id")
    TaskEntry loadTodoById(int id);
}

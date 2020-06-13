package com.example.todomvvm.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TaskEntry.class, Reminder.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object(); //1.??
    private static String DATABASE_NAME = "todolist";
    private static AppDatabase sInstance;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(1); //creates a different thread for db operations

    public static AppDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG,"Creating a new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,AppDatabase.DATABASE_NAME)
                        //.allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG,"Getting the database instance");
        return sInstance;
    }

    public abstract TaskDao taskDao();

}

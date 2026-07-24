package com.example.to_dolist.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.to_dolist.entity.Task;

@Database(
        entities = {Task.class},
        version = 1,
        exportSchema = false
)

public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

}
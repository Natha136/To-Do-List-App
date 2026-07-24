package com.example.to_dolist.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private static AppDatabase database;

    public static AppDatabase getInstance(Context context){

        if(database == null){

            database = Room.databaseBuilder(
                            context,
                            AppDatabase.class,
                            "todo_database"
                    )
                    .allowMainThreadQueries()
                    .build();

        }

        return database;

    }

}
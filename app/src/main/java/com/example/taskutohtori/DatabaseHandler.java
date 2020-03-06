package com.example.taskutohtori;

import android.content.Context;

import androidx.room.Room;

public class DatabaseHandler {

    DatabaseT database;

    public DatabaseHandler(Context context) {
        this.database = Room.databaseBuilder(context, DatabaseT.class, "Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }
}

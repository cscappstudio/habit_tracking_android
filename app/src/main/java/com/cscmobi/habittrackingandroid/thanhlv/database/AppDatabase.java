package com.cscmobi.habittrackingandroid.thanhlv.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.cscmobi.habittrackingandroid.data.model.HabitCollection;
import com.cscmobi.habittrackingandroid.thanhlv.model.History;
import com.cscmobi.habittrackingandroid.thanhlv.model.Task;


@Database(entities = {Task.class, History.class, HabitCollection.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)

public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "data.db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract Dao dao();
}

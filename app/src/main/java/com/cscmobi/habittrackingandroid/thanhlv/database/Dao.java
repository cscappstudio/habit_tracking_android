package com.cscmobi.habittrackingandroid.thanhlv.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.cscmobi.habittrackingandroid.thanhlv.model.Task;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    //WorldTimeModel
    @Insert
    void insertWorldTime(Task task);

    @Delete
    void deleteWorldTime(Task task);

    @Query("SELECT * FROM Task")
    List<Task> getAllTask();

}

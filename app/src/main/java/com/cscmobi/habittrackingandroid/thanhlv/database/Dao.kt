package com.cscmobi.habittrackingandroid.thanhlv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cscmobi.habittrackingandroid.thanhlv.model.Task

@Dao
interface Dao {

    @Insert
    suspend fun insertWorldTime(task: Task)

    @Delete
    suspend fun deleteWorldTime(task: Task)

    @Query("SELECT * FROM Task")
    suspend fun getAllTask(): List<Task>



    @Query("SELECT * FROM task")
    suspend fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE id IN (:tasksId)")
    suspend fun loadAllByIds(tasksId: IntArray): List<Task>

    @Query("SELECT * FROM task WHERE name LIKE :name")
    suspend fun findByName(name: String): Task

    @Insert
    suspend fun insertAll(vararg users: Task)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Task)

    @Update
    suspend fun update(item: Task)

    @Delete
    suspend fun delete(user: Task)
}
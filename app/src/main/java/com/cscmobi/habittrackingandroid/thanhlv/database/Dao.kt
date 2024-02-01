package com.cscmobi.habittrackingandroid.thanhlv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert
    suspend fun insertWorldTime(task: Task)

    @Delete
    suspend fun deleteWorldTime(task: Task)

    @Query("SELECT * FROM Task")
    suspend fun getAllTask(): List<Task>



    @Query("SELECT * FROM task")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id IN (:tasksId)")
     fun loadAllByIds(tasksId: IntArray): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE name LIKE :name")
     fun findByName(name: String): Flow<Task>

    @Query("SELECT * FROM task WHERE id=:id")
     fun findById(id: Int): Flow<Task>
    @Insert
    fun insertAll(vararg users: Task)

    @Insert
     fun insertAllHistory(vararg data: History)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Task)

    @Update
    suspend fun update(item: Task)

    @Delete
    suspend fun delete(user: Task)



    @Query("SELECT * FROM Challenge")
    suspend fun getAllChallenge(): List<Challenge>


    @Query("SELECT * FROM Challenge WHERE joinedHistory != \"[]\"")
    suspend fun getMyChallenge(): List<Challenge>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChallenge(item: Challenge)


    @Update
    suspend fun updateChallenge(item: Challenge)




    @Query("SELECT * FROM Mood")
    suspend fun getMood(): List<Mood>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMood(item: Mood)



    @Query("SELECT * FROM history")
    fun getAllHistory() : Flow<List<History>>

    @Query("SELECT * FROM history WHERE date=:date")
    suspend fun getHistorybyDate(date: Long) : History?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(item: History)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollection(item: HabitCollection)


    @Query("SELECT * FROM habitcollection")
    fun getAllCollection(): Flow<List<HabitCollection>>

    @Update
    suspend fun updateCollection(item: HabitCollection)

    @Delete
    suspend fun deleteCollection(item: HabitCollection)


    @Transaction
    suspend fun insertHistoryifNotExit(item: History): Boolean {
        if (item.date != null) {
            val existingEntity = getHistorybyDate(item.date!!)
            return if (existingEntity == null) {
                insertHistory(item)
                true
            } else {
                updateHistory(item)
                false
            }
        }
        return  false
    }


    @Update
    suspend fun updateHistory(history: History)


    @Query("UPDATE history SET taskInDay = :newTaskInDay WHERE id = :id")

    suspend fun deleteTaskinHistory(id: Int,newTaskInDay: List<TaskInDay>)

    @Query("SELECT * FROM history WHERE date >= :startDate")
    fun getHistoryWithDate(startDate: Long) : Flow<List<History>>



}
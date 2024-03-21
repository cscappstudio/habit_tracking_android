package com.cscmobi.habittrackingandroid.thanhlv.database

import android.accessibilityservice.AccessibilityService.TakeScreenshotCallback
import androidx.room.*
import androidx.room.Dao
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.Mood
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Tags
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert
    suspend fun insertWorldTime(task: Task)

    @Delete
    suspend fun deleteWorldTime(task: Task)

    @Query("SELECT * FROM task")
    fun getAllTask(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id IN (:tasksId)")
    fun loadAllTaskByIds(tasksId: LongArray): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE name LIKE :name")
    fun findTaskByName(name: String): Flow<Task>

    @Query("SELECT * FROM task WHERE id=:id")
    fun findTaskById(id: Long): Flow<Task>

    @Insert
    suspend fun insertAllTask(vararg users: Task)

    @Insert
    suspend fun insertAllHistory(vararg data: History)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(item: Task) : Long

    @Update
    suspend fun updateTask(item: Task)

    @Delete
    suspend fun deleteTask(user: Task)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTaskById(id: Long)
    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskById(id: Long): Task?

    @Query("SELECT * FROM Challenge WHERE name LIKE :name")
    suspend fun findChallengeByName(name: String): Challenge

    @Query("SELECT * FROM Challenge")
    suspend fun getAllChallenge(): List<Challenge>


    @Query("SELECT * FROM Challenge WHERE joinedHistory != \"null\"")
    suspend fun getMyChallenge(): List<Challenge>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChallenge(item: Challenge)


    @Update
    suspend fun updateChallenge(item: Challenge)
    @Delete
    suspend fun deleteChallenge(challenge: Challenge)


    @Query("SELECT * FROM Mood")
    suspend fun getMood(): List<Mood>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMood(item: Mood)

    @Update
    suspend fun updateMood(item: Mood)


    @Query("SELECT * FROM history")
    suspend fun getAllHistory2(): List<History>

    @Query("SELECT * FROM history WHERE date >= :startDate AND date < :endDate")
    suspend fun getHistoryFromAUntilB(startDate: Long, endDate: Long): List<History>
    @Query("SELECT * FROM task")
    suspend fun getAllTask2(): List<Task>
    @Query("SELECT * FROM history")
    fun getAllHistory(): Flow<List<History>>

    @Query("SELECT * FROM history WHERE date=:date")
    fun getHistorybyDate(date: Long): Flow<History?>

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


    @Update
    suspend fun updateHistory(history: History)


    @Delete
    suspend fun deleteHistory(history: History)
    
    @Query("UPDATE history SET tasksInDay = :newTaskInDay, progressDay = :progressDay WHERE id = :id")

    suspend fun updateHistory2(id: Long, newTaskInDay: List<TaskInDay>, progressDay: Int)

    @Query("UPDATE history SET tasksInDay = :newTaskInDay WHERE id = :id")

    suspend fun deleteTaskinHistory(id: Long, newTaskInDay: List<TaskInDay>)

    @Query("SELECT * FROM history WHERE date >= :startDate")
    fun getHistoryWithDate(startDate: Long): Flow<List<History>>

    @Query("SELECT * FROM HabitCollection WHERE nameCollection = :name")
    suspend fun findCollectionByName(name: String): HabitCollection?

    @Query("SELECT * FROM Challenge WHERE joinedHistory != \"[]\" AND name IN (:names)")
    fun findAllChallengesByName(names: List<String>): Flow<List<Challenge>>



    @Query("SELECT * FROM history WHERE date=:date")
    fun getHistoryByDate2(date: Long): History?
    @Query("SELECT * FROM history WHERE date >= :startDate")
    suspend fun getHistoryWithDate2(startDate: Long): List<History>


    @Query("SELECT * FROM Tags")
    suspend fun getAllTags(): List<Tags>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(item: Tags)

}
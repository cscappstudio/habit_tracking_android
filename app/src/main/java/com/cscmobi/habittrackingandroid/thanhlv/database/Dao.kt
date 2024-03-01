package com.cscmobi.habittrackingandroid.thanhlv.database

import androidx.room.*
import androidx.room.Dao
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


    @Query("SELECT * FROM Mood")
    suspend fun getMood(): List<Mood>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMood(item: Mood)

    @Update
    suspend fun updateMood(item: Mood)


    @Query("SELECT * FROM history")
    fun getAllHistory(): Flow<List<History>>

    @Query("SELECT * FROM history WHERE date=:date")
    fun getHistorybyDate(date: Long): Flow<History?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: History)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollection(item: HabitCollection)


    @Query("SELECT * FROM habitcollection")
    fun getAllCollection(): Flow<List<HabitCollection>>

    @Update
    suspend fun updateCollection(item: HabitCollection)

    @Delete
    suspend fun deleteCollection(item: HabitCollection)


//    @Transaction
//    suspend fun insertHistoryifNotExit(item: History): Boolean {
//        if (item.date != null) {
//            val existingEntity = getHistorybyDate(item.date!!)
//            return if (existingEntity == null) {
//                insertHistory(item)
//                true
//            } else {
//                updateHistory(item)
//                false
//            }
//        }
//        return  false
//    }


    @Update
    suspend fun updateHistory(history: History)


    @Query("UPDATE history SET taskInDay = :newTaskInDay WHERE id = :id")

    suspend fun deleteTaskinHistory(id: Long, newTaskInDay: List<TaskInDay>)

    @Query("SELECT * FROM history WHERE date >= :startDate")
    fun getHistoryWithDate(startDate: Long): Flow<List<History>>

    @Query("SELECT * FROM HabitCollection WHERE nameCollection = :name")
    suspend fun findCollectionByName(name: String): HabitCollection?

    @Query("SELECT * FROM Challenge WHERE joinedHistory != \"[]\" AND name IN (:names)")
    fun findAllChallengesByName(names: List<String>): Flow<List<Challenge>>

}
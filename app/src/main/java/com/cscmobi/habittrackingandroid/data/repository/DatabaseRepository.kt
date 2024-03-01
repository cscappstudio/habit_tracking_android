package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun insertTask(task: Task)

    suspend fun getAllTask(): Flow<List<Task>>

    suspend fun updateTask(task: Task)

    suspend  fun getTaskById(id: Long) : Flow<Task>

    suspend fun deleteTask(task: Task)

    suspend fun getHistorybyDate(date: Long) : Flow<History?>

    suspend fun loadAllByIds(id: LongArray): Flow<List<Task>>

    suspend fun insertCollection(collection: HabitCollection)

    suspend fun getAllCollection() : Flow<List<HabitCollection>>

    suspend fun updateCollection(collection: HabitCollection)

    suspend fun deleteCollection(collection: HabitCollection)

    suspend fun insertTaskHistory(history: History)

    suspend fun getAllHistory() : Flow<List<History>>

    suspend fun insertHistory(history: History)

    suspend fun updateHistory(history: History)

    suspend fun deleteTaskInHistory(id: Long,newTaskInDay: List<TaskInDay>)

    suspend fun getHistoryWithDate(startDate: Long): Flow<List<History>>

    suspend fun getCollectionByName(name: String) : HabitCollection?

    suspend fun findAllChallengesByName(name:List<String>) : Flow<List<Challenge>>

    suspend fun getMyChallenge() : Flow<List<Challenge>>

    suspend fun updateChallenge(item: Challenge)
}
package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun insertTask(task: Task)

    suspend fun getAllTask(): Flow<List<Task>>

    suspend fun updateTask(task: Task)

    suspend  fun getTaskById(id: Int) : Flow<Task>

    suspend fun deleteTask(task: Task)

    suspend fun getHistorybyDate(date: Long) : History?

    suspend fun loadAllByIds(id: IntArray): Flow<List<Task>>

    suspend fun insertCollection(collection: HabitCollection)

    suspend fun getAllCollection() : Flow<List<HabitCollection>>

    suspend fun updateCollection(collection: HabitCollection)

    suspend fun deleteCollection(collection: HabitCollection)

    suspend fun insertTaskHistory(history: History)

    suspend fun getAllHistory() : Flow<List<History>>

    suspend fun insertHistoryifNotExit(history: History)
}
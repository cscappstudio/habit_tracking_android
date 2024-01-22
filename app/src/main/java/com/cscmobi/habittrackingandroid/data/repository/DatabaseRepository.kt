package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun insertTask(task: Task)

    suspend fun getAllTask(): Flow<List<Task>>
}
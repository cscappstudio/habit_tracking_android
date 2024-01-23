package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.thanhlv.database.Dao
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseRepositoryImpl(private val dao: Dao) : DatabaseRepository {
    override suspend fun insertTask(task: Task) {
        dao.insert(task)

    }

    override suspend fun getAllTask(): Flow<List<Task>> {
        return flow {
            emit(dao.getAll())
        }
    }

    override suspend fun updateTask(task: Task) {
        dao.update(task)
    }

    override suspend fun getTaskById(id: Int): Flow<Task> {
        return  flow {
            emit(dao.findById(id))
        }
    }


}
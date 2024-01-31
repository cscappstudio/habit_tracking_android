package com.cscmobi.habittrackingandroid.data.repository

import android.util.Log
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.thanhlv.database.Dao
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseRepositoryImpl(private val dao: Dao) : DatabaseRepository {
    override suspend fun insertTask(task: Task) {
        dao.insert(task)

    }

    override suspend fun getAllTask(): Flow<List<Task>> {
        return dao.getAll()
    }

    override suspend fun updateTask(task: Task) {
        dao.update(task)
    }

    override suspend fun getTaskById(id: Int): Flow<Task> {
        return  dao.findById(id)
    }

    override suspend fun deleteTask(task: Task) {
        dao.delete(task)
    }

    override suspend fun getHistorybyDate(date: Long): History? {
        return dao.getHistorybyDate(date)
    }

    override suspend fun loadAllByIds(id: IntArray): Flow<List<Task>> {
        return  dao.loadAllByIds(tasksId = id)
    }

    override suspend fun insertCollection(collection: HabitCollection) {
        dao.insertCollection(collection)
    }

    override suspend fun getAllCollection(): Flow<List<HabitCollection>> {
       return dao.getAllCollection()
    }

    override suspend fun updateCollection(collection: HabitCollection) {
        Log.d("Updateeeeeee", collection.toString())

        dao.updateCollection(collection)
        delay(500L)
    }

    override suspend fun deleteCollection(collection: HabitCollection) {
        dao.deleteCollection(collection)
    }

    override suspend fun insertTaskHistory(history: History) {
        dao.insertHistory(history)
    }

    override suspend fun getAllHistory(): Flow<List<History>> {
        return dao.getAllHistory()
    }

    override suspend fun insertHistoryifNotExit(history: History) {
        dao.insertHistoryifNotExit(history)
    }


}
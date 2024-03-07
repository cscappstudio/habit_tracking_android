package com.cscmobi.habittrackingandroid.data.repository

import android.util.Log
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.thanhlv.database.Dao
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class DatabaseRepositoryImpl(private val dao: Dao) : DatabaseRepository {
    override suspend fun insertTask(task: Task) {
        dao.insertTask(task)

    }

    override suspend fun getAllTask(): Flow<List<Task>> {
        return dao.getAllTask()
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(task)
    }

    override suspend fun getTaskById(id: Long): Flow<Task> {
        return dao.findTaskById(id)
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    override suspend fun getHistorybyDate(date: Long): Flow<History?> {
        return dao.getHistorybyDate(date)
    }

    override suspend fun loadAllByIds(id: LongArray): Flow<List<Task>> {
        return dao.loadAllTaskByIds(tasksId = id)
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

    override suspend fun insertHistory(history: History) {
        dao.insertHistory(history)
    }

    override suspend fun updateHistory(history: History) {
        dao.updateHistory(history)
    }

    override suspend fun deleteTaskInHistory(id: Long, newTaskInDay: List<TaskInDay>) {
        dao.deleteTaskinHistory(id, newTaskInDay)
    }

    override suspend fun getHistoryWithDate(startDate: Long): Flow<List<History>> {
        return dao.getHistoryWithDate(startDate)
    }

    override suspend fun getCollectionByName(name: String): HabitCollection? {
        return dao.findCollectionByName(name)
    }

    override suspend fun findAllChallengesByName(names: List<String>): Flow<List<Challenge>> {
        return dao.findAllChallengesByName(names)
    }

    override suspend fun getMyChallenge(): Flow<List<Challenge>> {
        return flowOf(dao.getMyChallenge())
    }

    override suspend fun updateChallenge(item: Challenge) {
        dao.updateChallenge(item)
    }

    override suspend fun deleteHistory(history: History) {
        dao.deleteHistory(history)
    }


}
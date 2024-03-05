package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.Tag
import com.cscmobi.habittrackingandroid.data.repository.CollectionRepository
import com.cscmobi.habittrackingandroid.data.repository.DatabaseRepository
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class CollectionViewModel constructor(
    private val repository: CollectionRepository,
    private val databaseRepository: DatabaseRepository,
    private val context: Context
) :
    BaseViewModel() {

    val userIntent = Channel<CollectionIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<CollectionState>(CollectionState.Idle)
    val state: StateFlow<CollectionState>
        get() = _state


    private val _taskSize = MutableStateFlow<Int>(-1)
    val taskSize: StateFlow<Int>
        get() = _taskSize

    var taskCollection = Task()

    private var listCollectionName = mutableListOf<String>()

    init {
        handleIntent()
        getAllTask()
    }

    private fun getAllTask() = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.getAllTask().collect{
            _taskSize.value = it.size
        }
    }

    fun setUp() {

    }

    fun isExistCollectionName(name: String): Boolean {
        var validName = listCollectionName.filter { it == name }
        Log.d("VALIDD", validName.toString())
        return !validName.isNullOrEmpty()
    }


    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is CollectionIntent.FetchCollection -> fetchCollections()
                    is CollectionIntent.PassItemCollection -> passCollectionItem(it.data)
                    is CollectionIntent.NotCreateCollection -> _state.value =
                        CollectionState.IdleCreateCollection

                    is CollectionIntent.CreateCollection -> createCollection(it.data)
                    is CollectionIntent.UpdateCollection -> updateCollection(it.data)
                    is CollectionIntent.DeleteCollection -> deleteCollection(it.data)

                    is CollectionIntent.CreateTaskToRoutine -> insertTask(it.task)
                    is CollectionIntent.PassTaskfromCollection -> _state.value =
                        CollectionState.GetTask(it.task)

                    is CollectionIntent.EditTask -> _state.value = CollectionState.EditTask(it.task)
//                    is CollectionIntent.EditCollectionTask -> _state.value = CollectionState.EditCollectionTask(it.task)
                    is CollectionIntent.UpdateTask -> updateTask(it.task)
                    else -> {}
                }
            }
        }
    }

    fun createCollection(data: HabitCollection) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertCollection(data)
            delay(500L)
        }
    }


    fun updateCollection(collection: HabitCollection) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d("UPDATECOLLECT", collection.toString())
                databaseRepository.updateCollection(collection.copy())
                delay(500L)
            }
        } catch (e: Exception) {
            Log.d("UPDATECOLLECT", e.message.toString())

        }

    }

    fun setStateUpdateCollection(data: HabitCollection) {
        _state.value = CollectionState.UpdateCollection(data)
    }

    fun deleteCollection(collection: HabitCollection) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.deleteCollection(collection)
            delay(500L)
        }
    }


    fun insertTask(task: Task) = viewModelScope.launch {
        try {
            databaseRepository.insertTask(task)
            _state.value = CollectionState.CreateTaskRoutineSuccess(true)

        } catch (e: Exception) {
            Log.d("ERRRR", e.message.toString())

            _state.value = CollectionState.CreateTaskRoutineSuccess(false)
        }
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        try {
            Log.d("UPDATETASK", task.toString())
            databaseRepository.updateTask(task)
        } catch (e: Exception) {

        }
    }

    private fun fetchCollections() {
        viewModelScope.launch {
            _state.value = CollectionState.Loading
            try {
                combine(
                    flowOf(repository.getListLocalCollection()),
                    databaseRepository.getAllCollection()
                ) { localCollection, databaseCollection ->
                    val collections = mutableListOf<HabitCollection>()

                    collections.addAll(localCollection)
                    databaseCollection.forEach {
                        it.isEdit = true
                    }
                    collections.addAll(databaseCollection)
                    listCollectionName = collections.map { it.name }.toMutableList()
                    _state.value = CollectionState.Collections(collections)
                    Log.d("FETCHCOLLECTION", collections.toString())
                }.collect()


            } catch (e: Exception) {
                CollectionState.Collections(mutableListOf())
            }
        }
    }



    fun <T> merge(vararg flows: Flow<T>): Flow<T> = flowOf(*flows).flattenMerge()


    fun passCollectionItem(data: HabitCollection) {
        _state.value = CollectionState.Collection(data)
    }

    fun deleteTask(task: Task, deleteType: Int) = viewModelScope.launch {
        try {
            if (deleteType == 0) {
                databaseRepository.updateTask(task)
            } else if (deleteType == 1) {
                databaseRepository.deleteTask(task)
            }


        } catch (e: Exception) {

        }
    }

    fun deleteTaskInHistory(date: Long, taskId: Long) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                databaseRepository.getHistoryWithDate(date).collect {
                    it.forEach { history ->
                        val index = history.taskInDay.indexOfFirst { it.taskId == taskId }
                        if (index != -1) {
                            val newTaskInDay = history.taskInDay.toMutableList()
                            newTaskInDay.removeAt(index)
                            databaseRepository.deleteTaskInHistory(history.id, newTaskInDay)
                        }
                    }
                }

            }
        } catch (e: Exception) {
            println("chaulq______delete task in history: ${e.message}")
        }

    }

    fun deleteTaskInCollection(task: Task) = CoroutineScope(Dispatchers.IO).launch {
        task.collection?.let {
            var collection = async { databaseRepository.getCollectionByName(it) }.await()
            if (collection != null) {
                var newTask = collection.task?.toMutableList()
                newTask?.remove(task)

                val mCollection = collection.copy(task = newTask)
                databaseRepository.updateCollection(mCollection)
                passCollectionItem(mCollection)
            }

        }
    }

    fun updateTaskCollection(task: Task) = CoroutineScope(Dispatchers.IO).launch {
        task.collection?.let {
            var collection = async { databaseRepository.getCollectionByName(it) }.await()
            if (collection != null) {
                var newTask = collection.task?.toMutableList()
                newTask?.set(task.index, task)

                val mCollection = collection.copy(task = newTask)
                databaseRepository.updateCollection(mCollection)
                passCollectionItem(mCollection)
            }

        }

    }

    fun tag(): MutableList<Tag> {
        return mutableListOf(
            Tag("No tag"),
            Tag("Workout"),
            Tag("Clean room"),
            Tag("Morning routine"),
            Tag("Healthy lifestyle"),
            Tag("Relationship"),
            Tag("Sleep better"),
        )
    }

}
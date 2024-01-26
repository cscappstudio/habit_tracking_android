package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.Tag
import com.cscmobi.habittrackingandroid.data.repository.CollectionRepository
import com.cscmobi.habittrackingandroid.data.repository.DatabaseRepository
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionViewModel constructor(private val repository: CollectionRepository,private val databaseRepository: DatabaseRepository) :
    BaseViewModel() {

    val userIntent = Channel<CollectionIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<CollectionState>(CollectionState.Idle)
    val state: StateFlow<CollectionState>
        get() = _state


    init {
        handleIntent()
    }
    fun setUp(){}

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect{
                when(it) {
                    is CollectionIntent.FetchCollection -> fetchCollections()
                    is CollectionIntent.PassItemCollection -> passCollectionItem(it.data)
                    is CollectionIntent.NotCreateCollection -> _state.value = CollectionState.IdleCreateCollection
                    is CollectionIntent.CreateCollection -> createCollection(it.data)
                    is CollectionIntent.CreateTaskToRoutine -> insertTask(it.task)
                    is CollectionIntent.PassTaskfromCollection -> _state.value = CollectionState.GetTask(it.task)
                    is CollectionIntent.EditTask -> _state.value = CollectionState.EditTask(it.task)
                    is CollectionIntent.UpdateTask -> updateTask(it.task)
                    else -> {}
                }
            }
        }
    }

     fun createCollection(data: HabitCollection) {
         viewModelScope.launch {
             _state.value = CollectionState.CreateCollection(data)
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
        }catch (e: Exception){

        }
    }

    private fun fetchCollections() {
        viewModelScope.launch {
            _state.value = CollectionState.Loading
            _state.value = try {
                CollectionState.Collections(repository.getListLocalCollection())
            } catch (e: Exception) {
                CollectionState.Collections(mutableListOf())
            }
        }
    }

    fun passCollectionItem(data: HabitCollection) {
        _state.value = CollectionState.Collection(data)
    }

    fun tag(): MutableList<Tag> {
        return  mutableListOf(
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
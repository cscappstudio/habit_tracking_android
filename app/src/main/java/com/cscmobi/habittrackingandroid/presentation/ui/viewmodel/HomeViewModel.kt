package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.repository.DatabaseRepository
import com.cscmobi.habittrackingandroid.data.repository.HomeRepository
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.intent.HomeIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.HomeState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate


class HomeViewModel(private val repository: HomeRepository,private val databaseRepository: DatabaseRepository) : BaseViewModel() {
    var listWeekData = arrayListOf<LocalDate>()
    var currentWeekPos = -1

    val userIntent = Channel<HomeIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<HomeState>(HomeState.Empty)
    var tasks = mutableListOf<Task>()
    val state: StateFlow<HomeState>
        get() = _state

//    private val _categoryState = MutableStateFlow(mutableListOf<String>())
//
//    val categoryState: StateFlow<MutableList<String>> = _categoryState


    init {
        handleIntent()
//        test()

    }

    fun test() = viewModelScope.launch {
      databaseRepository.getAllTask().collect{


       }
    }


    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.FetchTasks -> fetchTasks()
                    is HomeIntent.FetchTasksbyCategory -> {
                        fetchTasksbyCategory(it.tag)
                    }

                    is HomeIntent.UpdateTask -> updateTask(it.task)
                    is HomeIntent.DeleteTask -> deleteTask(it.task,it.typeDelete)

                    else -> {}
                }
            }
        }
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        try {
            databaseRepository.updateTask(task)
        }catch (e: Exception){

        }
    }


    /**
     * deleteType = 0 -> delete task in future -> update endDate of task
     * deleteType = 1 -> delete task
     * */
    private fun deleteTask(task: Task,deleteType: Int) = viewModelScope.launch {
        try {
            if (deleteType == 0) {
                databaseRepository.updateTask(task)
            }
            else if (deleteType == 1) {
                databaseRepository.deleteTask(task)
            }


        } catch (e: Exception) {

        }
    }

    private fun fetchTasksbyCategory(tag: String) {
        viewModelScope.launch {
            _state.value = try {
                if (tag == "All") HomeState.Tasks(tasks)
                else
                    HomeState.Tasks(tasks.filter { it.tag == tag })

            } catch (e: Exception) {
                HomeState.Tasks(arrayListOf())
            }
        }
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            tasks.clear()

            databaseRepository.getAllTask().collect{
                try {
                tasks = it.toMutableList()
                _state.value =
                    HomeState.Tasks(it)
                } catch (e: Exception) {
                    HomeState.Tasks(arrayListOf())
                }


            }

        }
    }




    fun initDateWeek() {
        listWeekData.clear()

        val c = LocalDate.now()
        val weekRange = 12L

        for (i in -weekRange until weekRange) {
            val startOfWeek =
                c.plusWeeks(i).with(DayOfWeek.MONDAY) // Set to the first day of the week (Monday)

            // Add all days of the week manually
            for (day in 0 until DayOfWeek.values().size) {
                val currentDay = startOfWeek.plusDays(day.toLong())
                listWeekData.add(currentDay)
            }
        }


        currentWeekPos = listWeekData.indexOfFirst { it == c }
        // Print the list of LocalDate objects
        println("chaulq___________${listWeekData[currentWeekPos]}")

    }

}
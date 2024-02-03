package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.model.EndDate
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.data.repository.DatabaseRepository
import com.cscmobi.habittrackingandroid.data.repository.HomeRepository
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.intent.HomeIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.HomeState
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Helper
import com.cscmobi.habittrackingandroid.utils.Helper.validateTask
import com.cscmobi.habittrackingandroid.utils.Utils.getCurrentCalenderWithoutHour
import com.cscmobi.habittrackingandroid.utils.Utils.getDayofMonth
import com.cscmobi.habittrackingandroid.utils.Utils.getDayofWeek
import com.cscmobi.habittrackingandroid.utils.Utils.getDayofYear
import com.cscmobi.habittrackingandroid.utils.Utils.getMonth
import com.cscmobi.habittrackingandroid.utils.Utils.getWeek
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import java.util.Calendar
import java.util.Date
import kotlin.math.abs
import kotlin.math.log


class HomeViewModel(
    private val repository: HomeRepository,
    private val databaseRepository: DatabaseRepository,
    private var db: AppDatabase
) : BaseViewModel() {
    var listWeekData = arrayListOf<LocalDate>()
    var currentWeekPos = -1

    val userIntent = Channel<HomeIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<HomeState>(HomeState.Empty)
    val state: StateFlow<HomeState>
        get() = _state
    var tasks = mutableListOf<Task>()
    var histories = mutableListOf<History>()
    private val _currentHistory = MutableStateFlow<History>(History(id = -1))
    val currentHistory: StateFlow<History>
        get() = _currentHistory

//    private val _categoryState = MutableStateFlow(mutableListOf<String>())
//
//    val categoryState: StateFlow<MutableList<String>> = _categoryState


    init {
        handleIntent()
        test()
    }

    fun insertTaskHistory(history: History,date: Long? = null) {
        if (history.taskInDay.isEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertHistory(history)
        }
    }

    fun getHistorybyDate(date: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.getHistorybyDate(date).collect {
                        _currentHistory.value = it ?: History(id = -1)
            }

        }
    }

    fun updateHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.updateHistory(history)
    }


    private fun getAllHistory() = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.getAllHistory().collect {
            histories = it.toMutableList()
            Log.d("history", it.toString())

        }
    }






    fun test() {
        viewModelScope.launch {
            getAllHistory()
        }

    }


    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.FetchTasksbyDate -> fetchTasksByDate(it.date)
                    is HomeIntent.FetchTasksbyCategory -> {
                        fetchTasksbyCategory(it.tag)
                    }

                    is HomeIntent.UpdateTask -> updateTask(it.task)
                    is HomeIntent.DeleteTask -> deleteTask(it.task, it.typeDelete)

                    is HomeIntent.InsertTaskHistory -> insertTaskHistory(it.history,it.date)
                    is HomeIntent.UpdateHistory -> updateHistory(it.history)

                    else -> {}
                }
            }
        }
    }

//    fun setUpHistoryTaskByDate(date: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            databaseRepository.getHistorybyDate(date)?.let {
//                val taskIds = it.taskInDay?.map { it.taskId }
//                taskIds?.let { ids ->
//                    withContext(Dispatchers.IO) {
//                        fetchUser(ids).collect {
//
//                        }
//                    }
//
//                }
//            }
//        }
//
//
//    }

    suspend fun fetchUser(taskIds: List<Int>): Flow<List<Task>> {
        return GlobalScope.async(Dispatchers.IO) {
            databaseRepository.loadAllByIds(taskIds.toIntArray())
        }.await()
    }

    private fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        try {
            databaseRepository.updateTask(task)
        } catch (e: Exception) {

        }
    }


     fun deleteTaskInHistory(date: Long, taskId: Int) {
        try {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.getHistoryWithDate(date).collect{
                it.forEach { history ->
                   val index =  history.taskInDay.indexOfFirst { it.taskId == taskId }
                    if (index != -1)  {
                        val newTaskInDay = history.taskInDay.toMutableList()
                        newTaskInDay.removeAt(index)
                        databaseRepository.deleteTaskInHistory(history.id,newTaskInDay)
                    }
                }
            }

        }
        }catch (e: Exception) {
            println("chaulq______delete task in history: ${e.message}")
        }

    }

    /**
     * deleteType = 0 -> delete task in future -> update endDate of task
     * deleteType = 1 -> delete task
     * */
    private fun deleteTask(task: Task, deleteType: Int) = viewModelScope.launch {
        try {
            if (deleteType == 0) {
                databaseRepository.updateTask(task)
            } else if (deleteType == 1) {
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


    fun fetchHistoryByDate() {

    }

    private fun fetchTasksByDate(date: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tasks.clear()

            databaseRepository.getAllTask().collect {
                try {
                    var taskFilter = it.filter { validateTask(it, date) }


                    tasks = taskFilter.toMutableList()
                    _state.value = HomeState.Tasks(tasks)
                    // _state.value = HomeState.Tasks(taskFilter)

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
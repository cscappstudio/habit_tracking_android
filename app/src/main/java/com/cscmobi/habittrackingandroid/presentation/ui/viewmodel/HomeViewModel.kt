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

//    private val _categoryState = MutableStateFlow(mutableListOf<String>())
//
//    val categoryState: StateFlow<MutableList<String>> = _categoryState


    init {
        handleIntent()
        test()
    }

     fun insertTaskHistory(idTaskToday: List<Int>) {
        if (idTaskToday.isEmpty()) return

        var taskInDay = idTaskToday.map { TaskInDay(taskId = it) }
//        histories.forEach {
//            history ->
//            taskInDay.forEach { taskInfo ->
//                val taskIdInfo = history.taskInDay?.indexOfFirst { it.taskId == taskInfo.taskId }
//                if (taskIdInfo != null && taskIdInfo != -1 && history.taskInDay != null) {
//                    //taskInfo = history.taskInDay[taskIdInfo]
//                    taskInfo.currentStreak = history.taskInDay!![taskIdInfo].currentStreak
//                    taskInfo.longStreak = history.taskInDay!![taskIdInfo].longStreak
//                }
//
//            }
//
//        }
        viewModelScope.launch(Dispatchers.IO) {
           val test =  databaseRepository.insertHistoryifNotExit(
                History(date = getCurrentCalenderWithoutHour().time.time, taskInDay = taskInDay)
            )

        }
    }



    private fun getAllHistory() = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.getAllHistory().collect {
            histories = it.toMutableList()
        }
    }

    private fun getCurrentCalenderWithoutHour(): Calendar {
        val calendar = Calendar.getInstance()
        // Set hour, minute, second, and millisecond to zero
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    private fun getDateWithoutHour(date: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date(date)
        // Set hour, minute, second, and millisecond to zero
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time.time
    }

    fun test() {
        viewModelScope.launch {
            databaseRepository.getAllHistory().collect{
                Log.d("history", it.toString())
            }

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

                    is HomeIntent.InsertTaskHistory -> insertTaskHistory(it.taskIds)

                    else -> {}
                }
            }
        }
    }

    fun setUpHistoryTaskByDate(date: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.getHistorybyDate(date)?.let {
                val taskIds = it.taskInDay?.map { it.taskId }
                taskIds?.let { ids ->
                    withContext(Dispatchers.IO) {
                        fetchUser(ids).collect {

                        }
                    }

                }
            }
        }


    }

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

    private fun validateTask(task: Task, date: Long): Boolean {
        var isValid = true

        task.pauseDate?.let {
            var c = Calendar.getInstance()
            c.time = Date(it)
            c.add(Calendar.DAY_OF_MONTH, task.pause)

            if (c.time.time > date) {
                isValid = false
            }

            Log.d("isValid", "1 $isValid")
        }

        task.repeate?.let {
            if (it.isOn == true) {
                when (it.type) {
                    "daily" -> {

                        isValid =
                            abs(getDayofYear(date) - getDayofYear(task.startDate!!)) % it.frequency == 0


                    }

                    "monthly" -> {
                        var checkMonthValid =
                            abs(getMonth(date) - getMonth(task.startDate!!)) % it.frequency == 0
                        if (checkMonthValid) {
                            it.days?.forEach {

                            }
                            val dayValid = it.days?.find { getDayofMonth(date) == it }
                            isValid = (dayValid != null && dayValid != -1)
                        } else isValid = false

                    }

                    "weekly" -> {
                        var checkWeekValid =
                            abs(getWeek(date) - getWeek(task.startDate!!)) % it.frequency == 0
                        if (checkWeekValid) {
                            var dayValid = it.days?.find { it == getDayofWeek(date) }
                            isValid = dayValid != null && dayValid != -1


                        } else isValid = false


                    }
                }
                Log.d("isValid", "2 $isValid")

            }

        }

        val _date = getDateWithoutHour(date)
        if (getDateWithoutHour(task.startDate!!) > _date || (task.endDate!!.isOpen == true && task.endDate!!.endDate!! < _date)) {
            isValid = false
            Log.d("isValid", "3 $isValid")

        }

        if (isValid == false) Log.d("isvalid", task.name.toString())
        return isValid
    }

    private fun getDayofYear(date: Long): Int {
        var c = Calendar.getInstance()
        c.time = Date(date)
        return c.get(Calendar.DAY_OF_YEAR)
    }

    private fun getDayofMonth(date: Long): Int {
        var c = Calendar.getInstance()
        c.time = Date(date)
        return c.get(Calendar.DAY_OF_MONTH)
    }

    private fun getDayofWeek(date: Long): Int {
        var c = Calendar.getInstance()
        c.time = Date(date)
        return c.get(Calendar.DAY_OF_WEEK)
    }

    private fun getMonth(date: Long): Int {
        var c = Calendar.getInstance()
        c.time = Date(date)
        return c.get(Calendar.MONTH)
    }


    private fun getWeek(date: Long): Int {
        var c = Calendar.getInstance()
        c.time = Date(date)
        return c.get(Calendar.WEEK_OF_YEAR)
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
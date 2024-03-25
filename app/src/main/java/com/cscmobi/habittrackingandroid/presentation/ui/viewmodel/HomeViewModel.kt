package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.model.ChallengeTask
import com.cscmobi.habittrackingandroid.data.model.InfoTask
import com.cscmobi.habittrackingandroid.data.repository.DatabaseRepository
import com.cscmobi.habittrackingandroid.data.repository.HomeRepository
import com.cscmobi.habittrackingandroid.presentation.ui.intent.HomeIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.HomeState
import com.cscmobi.habittrackingandroid.thanhlv.database.AppDatabase
import com.cscmobi.habittrackingandroid.thanhlv.model.Challenge
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Constant.IDLE
import com.cscmobi.habittrackingandroid.utils.Helper.validateTask
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt


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

    private val _histories = MutableStateFlow<MutableList<History>>(mutableListOf())
    val histories: StateFlow<MutableList<History>>
        get() = _histories

    private val _currentHistory = MutableStateFlow<History>(History(id = IDLE))
    val currentHistory: StateFlow<History>
        get() = _currentHistory


    private val _challenges = MutableStateFlow<MutableList<Challenge>>(mutableListOf())
    val challenges: StateFlow<MutableList<Challenge>>
        get() = _challenges


    private val _challengeTaskMap = MutableStateFlow<Map<String, List<ChallengeTask>>>(mapOf())
    val challengeTaskMap: StateFlow<Map<String, List<ChallengeTask>>>
        get() = _challengeTaskMap
//    var : Map<String?, List<Task>> = mapOf()

    init {
        handleIntent()
        getAllHistory()
        getMyChallenge()
    }

    fun insertTaskHistory(history: History, date: Long? = null) {
        if (history.tasksInDay.isEmpty()) return

        Log.d("LOGINSERTHISTORY", history.toString())
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.insertHistory(history)
        }
    }

    fun getHistoryByDate(date: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.getHistoryByDate(date).collect {
                _currentHistory.value = it ?: History(id = -1)
            }

        }
    }

    fun updateHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {

        if (history.tasksInDay.isEmpty()) {
//            databaseRepository.deleteHistory(history)
        } else {
            var doneNum = 0
            var taskNum = 0
            history.tasksInDay.forEach {
                if (it.progress >= 100 && !it.isPaused) doneNum++
                if (!it.isPaused) taskNum++
            }
            if (taskNum == 0) history.progressDay = 0
            else history.progressDay = (doneNum * 100f / taskNum).roundToInt()
            databaseRepository.updateHistory(history)
        }
    }

    private fun getMyChallenge() = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.getMyChallenge().collect {
            _challenges.value = it.toMutableList()
        }
    }

    fun updateChallenge(challenge: Challenge) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.updateChallenge(challenge)
        }
    }


    private fun getAllHistory() = viewModelScope.launch(Dispatchers.IO) {

        databaseRepository.getAllHistory().collect {
            if (it.isEmpty()) return@collect

            val histories = it.toMutableList()

            databaseRepository.getAllTask().collect { tasks ->
                var challengeTaskMap = tasks.filter { it.challenge.isNotEmpty() }
                    .map { ChallengeTask(it.challenge, InfoTask(it.id, it.goal!!.target)) }
                    .groupBy { it.challengeName }

                if (histories.size > 2)
                    challengeTaskMap.forEach { t, u ->
                        u.forEach { challengeTask ->
                            val validIndex =
                                histories[histories.size - 2].tasksInDay.indexOfFirst { it.taskId == challengeTask.infoTask.id }
                            if (validIndex != -1) challengeTask.infoTask.status =
                                histories[histories.size - 2].tasksInDay[validIndex].progress >= challengeTask.infoTask.target
                        }
                    }
                else challengeTaskMap = mapOf()

                var currentDayStreak = 0
                for (i in histories.size - 1 downTo 0) {
                    var taskFinish = true

                    histories[i].tasksInDay.forEach { taskInDay ->
                        tasks.find { it.id == taskInDay.taskId }?.let {
                            if (it.goal!!.target != taskInDay.progressGoal) {
                                taskFinish = false
                            }
                        }

                        if (!taskFinish) {
                            return@forEach
                        }
                    }
                    if (taskFinish) currentDayStreak++
                    else break
                }
                println("chaulqalo___$currentDayStreak")
                histories.last().currentStreakDay = currentDayStreak
                _histories.value = histories
                _challengeTaskMap.value = challengeTaskMap

            }
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.FetchTasksByDate -> fetchTasksByDate(it.date)
                    is HomeIntent.FetchTasksByCategory -> {
                        fetchTasksbyCategory(it.tag)
                    }

                    is HomeIntent.UpdateTask -> updateTask(it.task)
                    is HomeIntent.DeleteTask -> deleteTask(it.task, it.typeDelete)

                    is HomeIntent.InsertTaskHistory -> insertTaskHistory(it.history, it.date)
                    is HomeIntent.UpdateHistory -> updateHistory(it.history)

                    else -> {}
                }
            }
        }
    }

    suspend fun fetchUser(taskIds: List<Long>): Flow<List<Task>> {
        return GlobalScope.async(Dispatchers.IO) {
            databaseRepository.loadAllByIds(taskIds.toLongArray())
        }.await()
    }

    private fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        try {
            databaseRepository.updateTask(task)
        } catch (e: Exception) {

        }
    }

    fun deleteTaskInHistory(date: Long, taskId: Long) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                databaseRepository.getHistoryWithDate(date).collect {
                    it.forEach { history ->
                        val index = history.tasksInDay.indexOfFirst { it.taskId == taskId }
                        if (index != -1) {

                            val newTaskInDay = history.tasksInDay.toMutableList()
                            newTaskInDay.removeAt(index)
                            // databaseRepository.deleteTaskInHistory(history.id, newTaskInDay)
                            history.tasksInDay = newTaskInDay
                            updateHistory(history)
                        }
                    }
                }

            }
        } catch (e: Exception) {
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
            Log.d("fetchTasksByDate", "fetchTasksByDate")

            _state.value = try {
                if (tag == "   All   ") HomeState.Tasks(tasks)
                else
                    HomeState.Tasks(tasks.filter { it.tag == tag })

            } catch (e: Exception) {
                HomeState.Tasks(arrayListOf())
            }
        }
    }


    private fun fetchTasksByDate(date: Long) {

        viewModelScope.launch(Dispatchers.IO) {
            tasks.clear()

            databaseRepository.getAllTask().collect { tasks ->
                try {

                    val taskFilter = tasks.filter { validateTask(it, date) }

                    println("thanhlv fetchTasksByDate ---- " + date + " /// " + taskFilter)

                    this@HomeViewModel.tasks = taskFilter.toMutableList()

//                    if (date > Helper.currentDate.toDate()) {
//                        this@HomeViewModel.tasks.forEach {
//                            it.goal?.currentProgress = 0
//                        }
//                    }

                    if (this@HomeViewModel.tasks.isEmpty())
                        _state.value = HomeState.Empty
                    else {
                        this@HomeViewModel.tasks[0].makeDiffRan = (0..1000).random()
                        _state.value = HomeState.Tasks(this@HomeViewModel.tasks)
                    }


                } catch (e: Exception) {
                    _state.value = HomeState.Tasks(arrayListOf())
                }


            }

        }
    }


    fun initDateWeek(date: Long? = null) {

        var c = LocalDate.now()
        val minRange = c.minusWeeks(12).toDate()
        val maxRange = c.plusWeeks(12).toDate()


        if (date != null) {
            var calendar = Calendar.getInstance()
            calendar.time = Date(date)

            if (calendar.time.time.toDate() in minRange..maxRange) return
            c = LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        listWeekData.clear()


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


        currentWeekPos = listWeekData.indexOfFirst { it == LocalDate.now() }
        // Print the list of LocalDate objects
        println("chaulq___________${currentWeekPos}")
    }

}
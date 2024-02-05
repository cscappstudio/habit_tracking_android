package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.model.DataTaskHistory
import com.cscmobi.habittrackingandroid.data.model.RemindTask
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.data.model.TaskRepeat
import com.cscmobi.habittrackingandroid.data.repository.DatabaseRepository
import com.cscmobi.habittrackingandroid.presentation.ui.intent.DetailTaskIntent
import com.cscmobi.habittrackingandroid.presentation.ui.intent.HomeIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.DetailTaskState
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.utils.Utils
import com.cscmobi.habittrackingandroid.utils.Utils.getDayofMonth
import com.cscmobi.habittrackingandroid.utils.Utils.getMonth
import com.cscmobi.habittrackingandroid.utils.Utils.toDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.abs

class DetailTaskViewModel(private val databaseRepository: DatabaseRepository): BaseViewModel() {


    val userIntent = Channel<DetailTaskIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<DetailTaskState>(DetailTaskState.Idle)
    val state: StateFlow<DetailTaskState>
        get() = _state

    private val _history =  MutableStateFlow<List<History>>(emptyList())

    val history : StateFlow<List<History>>
        get() = _history

//    var fakeDataHistory = mutableListOf(
//    History(
//    0,0,
//    listOf(
//    TaskInDay(
//    0,0,0
//    ),
//    TaskInDay(3,50,0,0)
//    )
//    ),
//
//    History(
//    1,0, listOf(TaskInDay(3,100,1,1))
//    ),
//    History(
//    2,0, listOf(TaskInDay(3,80,1,1))
//    ),
//    History(
//    3,0, listOf(TaskInDay(3,0,0,1))
//    ),
//    History(
//    4,0, listOf(TaskInDay(3,100,1,1))
//    ),
//    History(
//    5, 0,listOf(TaskInDay(3,100,2,2))
//    ),
//    History(
//    6,0, listOf(TaskInDay(3,100,3,3))
//    ),
//    History(
//    7, 0,listOf(TaskInDay(3,66,0,3))
//    ),
//    History(
//    8,0, listOf(TaskInDay(3,100,1,3))
//    ),
//    )

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect{
                when(it) {
                    is DetailTaskIntent.FetchTaskbyId -> getTaskById(it.id)

                    is DetailTaskIntent.fetchHistoryByTask -> fetchHistoryByTask(it.task)
                    is DetailTaskIntent.UpdateTask -> updateTask(it.task)
                    is DetailTaskIntent.DeleteTask -> deleteTask(it.task,it.typeDelete)
                    else -> {}
                }
            }
        }
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        try {
            databaseRepository.updateTask(task)
            delay(500L)
        }catch (e: Exception){

        }
    }

    fun updateHistory(history: History) = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.updateHistory(history)
    }


    /**
     * deleteType = 0 -> delete task in future -> update endDate of task
     * deleteType = 1 -> delete task
     * */
    private fun deleteTask(task: Task, deleteType: Int) = viewModelScope.launch {
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

   fun fetchHistoryByTask(task: Task) {
       viewModelScope.launch {
//           try {
//               var c = Calendar.getInstance()
//               fakeDataHistory.forEach {
//                   it.date = c.time.time
//                   c.add(Calendar.DAY_OF_MONTH,-1)
//               }
//
//               _history.value = fakeDataHistory
//
//           }catch (e: Exception) {
//               _history.value = mutableListOf()
//           }


           databaseRepository.getHistoryWithDate(task.startDate!!.toDate()).collect{
               var filterHistory = it.toMutableList()

               task.repeate?.let {
                   repeat ->
                   if (repeat.isOn == true) {
                       filterHistory.clear()
                       repeat.frequency
                       repeat.days
                       when(repeat.type) {
                           "daily" -> {
                               filterHistory = it.filter {    abs(Utils.getDayofYear(it.date!!) - Utils.getDayofYear(task.startDate!!)) % repeat.frequency == 0 }
                                   .toMutableList()
                           }
                           "weekly" -> {
                               it.forEach {history ->
                               var checkWeekValid =
                                       abs(Utils.getWeek(history.date!!) - Utils.getWeek(task.startDate!!)) % repeat.frequency == 0
                                   if (checkWeekValid) {
                                       var dayValid = repeat.days?.find { it == Utils.getDayofWeek(history.date!!) }
                                       val isValid = dayValid != null && dayValid != -1
                                       if (isValid) filterHistory.add(history)
                                   }
                               }

                           }
                           "monthly" -> {
                               it.forEach { history ->
                                   var checkMonthValid =
                                       abs(getMonth(history.date!!) - getMonth(task.startDate!!)) % repeat.frequency == 0

                                   if (checkMonthValid) {

                                       val dayValid = repeat.days?.find { getDayofMonth(history.date!!) == it }
                                       val isValid = (dayValid != null && dayValid != -1)
                                       if (isValid) filterHistory.add(history)

                                   }
                               }
                           }
                       }

                   }
               }

               _history.value = filterHistory
           }

       }
   }
    var listDataTasHistory = mutableListOf<DataTaskHistory>()
    fun getDetailHistory(history: List<History>, taskId: Int): MutableList<DataTaskHistory> {
        listDataTasHistory.clear()
        history.forEach {
          it.taskInDay?.forEach { taskInfo ->
              if (taskInfo.taskId == taskId) {
                  listDataTasHistory.add(DataTaskHistory(taskInfo,it.date ?: 0))
              }
          }
        }
        return  listDataTasHistory
    }


    fun  getTaskById(id: Int) {
        viewModelScope.launch {
            try {
            databaseRepository.getTaskById(id).collect{
                _state.value =  DetailTaskState.TaskById(it)
            }
            } catch (e: Exception) {
                _state.value =  DetailTaskState.Idle
            }
        }
    }


    fun showRepeatString(repeat: TaskRepeat?): String {

        if (repeat != null && repeat.isOn == true) {
            var days: String = when (repeat.type) {
                "weekly" -> Utils.mapNumbersToDayNames(repeat.days?.toIntArray() ?: intArrayOf(2))
                    .joinToString(", ")

                "monthly" -> Utils.addOrdinalSuffixToDays(repeat.days?.toIntArray() ?: intArrayOf())
                    .joinToString(", ")

                else -> ""
            }

            if (days.isNotEmpty()) {
                days = "on $days"
            }


            val frequency = if ((repeat.frequency ?: 0) > 1) repeat.frequency.toString() else ""
            val type = when(repeat.type) {
                "weekly" -> if ((repeat.frequency ?: 0) > 1) "weeks" else "week"
                "monthly" -> if ((repeat.frequency ?: 0) > 1) "months" else "month"
                else -> if ((repeat.frequency ?: 0) > 1) "days" else "day"
            }

            return  "Repeat: every $frequency $type $days"

        }
        return    "Repeat: No Repeat"
    }

    fun showReminder(remindTask: RemindTask?): String {
       if (remindTask != null && remindTask.isOpen == true) {
           val formattedTime = String.format("%d:%02d %s", remindTask.hour, remindTask.minute, remindTask.unit)

          return "Reminder: $formattedTime"
       }

        return  "Reminder: No Reminder"
    }



}
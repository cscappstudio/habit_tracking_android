package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.R
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
import com.cscmobi.habittrackingandroid.utils.Helper
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
import java.util.Date
import kotlin.math.abs

class DetailTaskViewModel(private val databaseRepository: DatabaseRepository): BaseViewModel() {


    val userIntent = Channel<DetailTaskIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<DetailTaskState>(DetailTaskState.Idle)
    val state: StateFlow<DetailTaskState>
        get() = _state

    private val _history =  MutableStateFlow<List<History>>(emptyList())

    val history : StateFlow<List<History>>
        get() = _history

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

    fun deleteTaskInHistory(date: Long, taskId: Long) {
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
           databaseRepository.getHistoryWithDate(task.startDate!!.toDate()).collect{
               var filterHistory = it.toMutableList()
                println("chaulq_history______________________________$filterHistory")
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
    fun getDetailHistory(history: List<History>, taskId: Long): MutableList<DataTaskHistory> {
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

    fun getDetailHistoryTest(taskId: Long): MutableList<DataTaskHistory> {
        listDataTasHistory.clear()
        val c = Calendar.getInstance()
        c.time.time = Helper.currentDate.toDate()
        c.add(Calendar.DAY_OF_MONTH,-1)

        listDataTasHistory.add(DataTaskHistory(TaskInDay(taskId,100,1),c.time.time))
        c.add(Calendar.DAY_OF_MONTH,1)
        listDataTasHistory.add(DataTaskHistory(TaskInDay(taskId,50,0),c.time.time))
        c.add(Calendar.DAY_OF_MONTH,1)
        listDataTasHistory.add(DataTaskHistory(TaskInDay(taskId,0,0),c.time.time))
        c.add(Calendar.DAY_OF_MONTH,1)
        listDataTasHistory.add(DataTaskHistory(TaskInDay(taskId,30,0),c.time.time))
        c.add(Calendar.DAY_OF_MONTH,1)
        listDataTasHistory.add(DataTaskHistory(TaskInDay(taskId,100,1),c.time.time))
        c.add(Calendar.DAY_OF_MONTH,1)
        listDataTasHistory.add(DataTaskHistory(TaskInDay(taskId,0,1),c.time.time))
        c.add(Calendar.DAY_OF_MONTH,1)
        listDataTasHistory.add(DataTaskHistory(TaskInDay(taskId,100,1),c.time.time))
        c.add(Calendar.DAY_OF_MONTH,1)

        Log.d("getDetailHistoryTest", listDataTasHistory.toString())
        return  listDataTasHistory
    }


    fun  getTaskById(id: Long) {
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


    fun showRepeatString(repeat: TaskRepeat?,context: Context): String {

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

            return  "${context.getString(R.string.repeat)}: every $frequency $type $days"

        }
        return context.getString(R.string.repeat_no_repeat)
    }

    fun showReminder(remindTask: RemindTask?,context: Context): String {
       if (remindTask != null && remindTask.isOpen == true) {
           val formattedTime = String.format("%d:%02d %s", remindTask.hour, remindTask.minute, remindTask.unit)

          return "${context.getString(R.string.reminder)}: $formattedTime"
       }

        return context.getString(R.string.reminder_no_reminder)
    }



}
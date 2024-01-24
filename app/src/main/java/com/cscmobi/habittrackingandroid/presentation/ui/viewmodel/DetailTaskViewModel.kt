package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.model.History
import com.cscmobi.habittrackingandroid.data.model.RemindTask
import com.cscmobi.habittrackingandroid.data.model.TaskRepeat
import com.cscmobi.habittrackingandroid.data.repository.DatabaseRepository
import com.cscmobi.habittrackingandroid.presentation.ui.intent.DetailTaskIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.DetailTaskState
import com.cscmobi.habittrackingandroid.utils.Utils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class DetailTaskViewModel(private val databaseRepository: DatabaseRepository): BaseViewModel() {


    val userIntent = Channel<DetailTaskIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<DetailTaskState>(DetailTaskState.Idle)
    val state: StateFlow<DetailTaskState>
        get() = _state


    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect{
                when(it) {
                    is DetailTaskIntent.FetchTaskbyId -> getTaskById(it.id)

                    else -> {}
                }
            }
        }
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
            "Repeat: every $frequency ${repeat.type} $days"

        }
        return    "Repeat: No Repeat"
    }

    fun showReminder(remindTask: RemindTask?): String {
       if (remindTask != null && remindTask.isOpen == true) {
            return "Reminder: ${remindTask.hour}:${remindTask.minute} ${remindTask.unit}"
       }

        return  "Reminder: No Reminder"
    }




}
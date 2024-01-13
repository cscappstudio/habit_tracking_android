package com.cscmobi.habittrackingandroid.presentation.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.cscmobi.habittrackingandroid.base.BaseViewModel
import com.cscmobi.habittrackingandroid.data.repository.HomeRepository
import com.cscmobi.habittrackingandroid.presentation.ui.intent.CollectionIntent
import com.cscmobi.habittrackingandroid.presentation.ui.intent.HomeIntent
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.HomeState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate


class HomeViewModel(private val repository: HomeRepository) : BaseViewModel() {
    var listWeekData = arrayListOf<LocalDate>()
    var currentWeekPos = -1

    val userIntent = Channel<HomeIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<HomeState>(HomeState.Empty)
    val state: StateFlow<HomeState>
        get() = _state
//    private val _categoryState = MutableStateFlow(mutableListOf<String>())
//
//    val categoryState: StateFlow<MutableList<String>> = _categoryState


    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.FetchTasks -> fetchTasks()
                    is HomeIntent.FetchTasksbyCategory -> {fetchTasksbyCategory(it.tag)}
                    else -> {}
                }
            }
        }
    }

    private fun fetchTasksbyCategory(tag: String) {
        viewModelScope.launch {
            _state.value = try {
                HomeState.Tasks(repository.getListTask().filter { it.tag == tag })

            } catch (e: Exception) {
                HomeState.Tasks(arrayListOf())
            }
        }
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            _state.value = try {
                HomeState.Tasks(repository.getListTask())

            } catch (e: Exception) {
                HomeState.Tasks(arrayListOf())
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
            listWeekData.add(startOfWeek)
        }

        currentWeekPos = listWeekData.indexOfFirst { it == c.with(DayOfWeek.MONDAY) }
        println("chaulq____________${currentWeekPos}")

    }


}
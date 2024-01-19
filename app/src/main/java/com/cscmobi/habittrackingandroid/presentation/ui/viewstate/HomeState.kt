package com.cscmobi.habittrackingandroid.presentation.ui.viewstate

import com.cscmobi.habittrackingandroid.data.dto.entities.Task


sealed class HomeState {
    object Empty: HomeState()
    object Loading: HomeState()

    data class Tasks(val tasks: List<Task>): HomeState()
}
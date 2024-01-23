package com.cscmobi.habittrackingandroid.presentation.ui.viewstate

import com.cscmobi.habittrackingandroid.thanhlv.model.Task

sealed class DetailTaskState {
    data class TaskById(val task: Task) : DetailTaskState()
    data object Idle : DetailTaskState()
}
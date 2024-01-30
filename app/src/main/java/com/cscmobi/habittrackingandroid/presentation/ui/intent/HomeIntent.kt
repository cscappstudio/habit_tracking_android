package com.cscmobi.habittrackingandroid.presentation.ui.intent

import com.cscmobi.habittrackingandroid.thanhlv.model.Task

sealed class HomeIntent {
    data object FetchTasks: HomeIntent()
    data class FetchTasksbyCategory(val tag: String): HomeIntent()

    data class UpdateTask(val task: Task): HomeIntent()

    data class DeleteTask(val task: Task, val typeDelete: Int): HomeIntent()
}
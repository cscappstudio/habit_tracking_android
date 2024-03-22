package com.cscmobi.habittrackingandroid.presentation.ui.intent

import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.thanhlv.model.Task

sealed class HomeIntent {
    data class FetchTasksByDate(val date: Long): HomeIntent()
    data class FetchTasksByCategory(val tag: String): HomeIntent()

    data class UpdateTask(val task: Task): HomeIntent()

    data class DeleteTask(val task: Task, val typeDelete: Int): HomeIntent()
    data class InsertTaskHistory(val history: History, var date: Long? = null): HomeIntent()
    data class UpdateHistory(val history: History): HomeIntent()
}
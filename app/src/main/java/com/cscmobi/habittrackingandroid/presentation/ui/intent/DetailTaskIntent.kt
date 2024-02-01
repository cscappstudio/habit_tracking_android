package com.cscmobi.habittrackingandroid.presentation.ui.intent

import com.cscmobi.habittrackingandroid.thanhlv.model.Task

sealed class DetailTaskIntent {
    data class FetchTaskbyId(val id: Int): DetailTaskIntent()

    data class fetchHistoryByTask(val task: Task) : DetailTaskIntent()

    data class UpdateTask(val task: Task): DetailTaskIntent()

    data class DeleteTask(val task: Task, val typeDelete: Int): DetailTaskIntent()

}
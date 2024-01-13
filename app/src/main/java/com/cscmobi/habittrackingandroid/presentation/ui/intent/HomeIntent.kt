package com.cscmobi.habittrackingandroid.presentation.ui.intent

sealed class HomeIntent {
    data object FetchTasks: HomeIntent()
    data class FetchTasksbyCategory(val tag: String): HomeIntent()
}
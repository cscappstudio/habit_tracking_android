package com.cscmobi.habittrackingandroid.presentation.ui.intent

sealed class HomeIntent {
    object FetchTasks: HomeIntent()
}
package com.cscmobi.habittrackingandroid.presentation.ui.intent

sealed class DetailTaskIntent {
    data class FetchTaskbyId(val id: Int): DetailTaskIntent()

}
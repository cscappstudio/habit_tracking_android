package com.cscmobi.habittrackingandroid.presentation.ui.intent

sealed class CollectionIntent {
    data object FetchCollection: CollectionIntent()
}
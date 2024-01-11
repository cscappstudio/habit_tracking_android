package com.cscmobi.habittrackingandroid.presentation.ui.viewstate

import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.Task

sealed class CollectionState {
    data object Idle : CollectionState()

    data object Loading: CollectionState()
    data class Collections(val collection: List<HabitCollection>): CollectionState()
}
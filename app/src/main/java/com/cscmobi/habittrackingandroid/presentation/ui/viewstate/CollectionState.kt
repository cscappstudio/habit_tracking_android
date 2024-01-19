package com.cscmobi.habittrackingandroid.presentation.ui.viewstate

import com.cscmobi.habittrackingandroid.data.model.HabitCollection

sealed class CollectionState {
    data object Idle : CollectionState()

    data object Loading: CollectionState()
    data class Collections(val collection: List<HabitCollection>): CollectionState()
    data class Collection(val data: HabitCollection): CollectionState()


    data object IdleCreateCollection: CollectionState()
    data class CreateCollection(val collection: HabitCollection) : CollectionState()
}
package com.cscmobi.habittrackingandroid.presentation.ui.intent

import com.cscmobi.habittrackingandroid.data.model.HabitCollection

sealed class CollectionIntent {
    data object FetchCollection: CollectionIntent()
    data class PassItemCollection(val data: HabitCollection): CollectionIntent()

    data object NotCreateCollection: CollectionIntent()
    data class CreateCollection(val data: HabitCollection): CollectionIntent()
}
package com.cscmobi.habittrackingandroid.presentation.ui.intent

import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task

sealed class CollectionIntent {
    data object FetchCollection: CollectionIntent()
    data class PassItemCollection(val data: HabitCollection): CollectionIntent()

    data object NotCreateCollection: CollectionIntent()
    data class CreateCollection(val data: HabitCollection): CollectionIntent()

    data class CreateTasToCollectionk(val task: Task): CollectionIntent()
    data class CreateTaskToRoutine(val task: Task): CollectionIntent()}
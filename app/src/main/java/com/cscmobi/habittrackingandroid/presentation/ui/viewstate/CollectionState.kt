package com.cscmobi.habittrackingandroid.presentation.ui.viewstate

import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.thanhlv.model.Task

sealed class CollectionState {
    data object Idle : CollectionState()

    data object Loading: CollectionState()
    data class Collections(val collection: List<HabitCollection>): CollectionState()
    data class Collection(val data: HabitCollection): CollectionState()


    data object IdleCreateCollection: CollectionState()
    data class CreateCollection(val collection: HabitCollection) : CollectionState()

    data class CreateTaskRoutineSuccess(val isSuccess: Boolean) : CollectionState()

//    data class CreateTasToCollectionk(val task: Task): CollectionState()
//    data class CreateTaskToRoutine(val task: Task): CollectionState()
}
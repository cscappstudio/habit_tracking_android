package com.cscmobi.habittrackingandroid.presentation.ui.viewstate

import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.thanhlv.model.Task

sealed class CollectionState {
    data object Idle : CollectionState()
    data object Loading: CollectionState()
    data class Collections(val collection: List<HabitCollection>): CollectionState()
    data class Collection(val data: HabitCollection): CollectionState()
    data class UpdateCollection(val data: HabitCollection): CollectionState()
    data object IdleCreateCollection: CollectionState()
    data class CreateCollection(val collection: HabitCollection) : CollectionState()
    data class CreateTaskRoutineSuccess(val isSuccess: Boolean) : CollectionState()
    data class GetTask(val task: Task): CollectionState()
    data class EditTask(val task: Task): CollectionState()
}
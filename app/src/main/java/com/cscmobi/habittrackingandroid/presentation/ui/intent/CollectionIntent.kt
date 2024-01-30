package com.cscmobi.habittrackingandroid.presentation.ui.intent

import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.presentation.ui.viewstate.CollectionState
import com.cscmobi.habittrackingandroid.thanhlv.model.Task

sealed class CollectionIntent {
    data object FetchCollection: CollectionIntent()
    data class PassItemCollection(val data: HabitCollection): CollectionIntent()

    data object NotCreateCollection: CollectionIntent()
    data class CreateCollection(val data: HabitCollection): CollectionIntent()
    data class UpdateCollection(val data: HabitCollection): CollectionIntent()
    data class DeleteCollection(val data: HabitCollection): CollectionIntent()

    data class CreateTaskToCollectionk(val task: Task): CollectionIntent()
    data class CreateTaskToRoutine(val task: Task): CollectionIntent()

    data class PassTaskfromCollection(val task: Task): CollectionIntent()
    data class EditTask(val task: Task): CollectionIntent()
//    data class EditCollectionTask(val task: Task): CollectionIntent()
    data class UpdateTask(val task: Task): CollectionIntent()
}


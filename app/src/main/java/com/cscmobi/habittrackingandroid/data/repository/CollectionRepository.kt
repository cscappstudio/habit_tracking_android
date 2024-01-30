package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
   suspend fun getListLocalCollection(): Flow<List<HabitCollection>>
    fun getListRemoteCollection(): List<HabitCollection>
}
package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.data.model.HabitCollection

interface CollectionRepository {
    fun getListLocalCollection(): List<HabitCollection>
    fun getListRemoteCollection(): List<HabitCollection>
}
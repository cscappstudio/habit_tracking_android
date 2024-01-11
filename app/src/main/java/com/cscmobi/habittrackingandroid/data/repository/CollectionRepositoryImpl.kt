package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.HabitCollection
import com.cscmobi.habittrackingandroid.data.model.Task

class CollectionRepositoryImpl : CollectionRepository {
    private val nightTimeTasks = mutableListOf<Task>(
        Task("Practice affirmations", R.drawable.ic_calendar),
        Task("Practice visualization", R.drawable.ic_calendar),
        Task("Practice visualization", R.drawable.ic_calendar),
        Task("Practice visualization", R.drawable.ic_calendar),
        Task("Practice visualization", R.drawable.ic_calendar),
        Task("Practice visualization", R.drawable.ic_calendar),
        Task("Practice visualization", R.drawable.ic_calendar),
    )

    private var localCollection: MutableList<HabitCollection> = mutableListOf(
        HabitCollection(
            R.drawable.bg_collection1, "Nighttime", nightTimeTasks, R.color.blue
        ),
        HabitCollection(
            R.drawable.bg_collection2, "Morning Routine", nightTimeTasks, R.color.pink
        ),
        HabitCollection(
            R.drawable.bg_collection3, "Heathy lifestyle", nightTimeTasks,  R.color.orange
        ),
        HabitCollection(
            R.drawable.bg_collection4, "Daily Chores", nightTimeTasks, R.color.green
        ),
        HabitCollection(
            R.drawable.bg_collection5, "Stress relief", nightTimeTasks, R.color.purple
        ),
        HabitCollection(
            R.drawable.bg_collection6, "Study", nightTimeTasks, R.color.blue
        ),
        HabitCollection(
            R.drawable.bg_collection7, "Personal finance", nightTimeTasks, R.color.pink
        ),
        HabitCollection(
            R.drawable.bg_collection8, "Staying fit", nightTimeTasks,  R.color.orange
        ),
        HabitCollection(
            R.drawable.bg_collection9, "Hit the Gym", nightTimeTasks, R.color.green
        ),
        HabitCollection(
            R.drawable.bg_collection10, "Self-improvement", nightTimeTasks, R.color.purple
        ),
    )

    override fun getListLocalCollection(): List<HabitCollection> {
            return  localCollection
    }

    override fun getListRemoteCollection(): List<HabitCollection> {
        return  mutableListOf()
    }
}
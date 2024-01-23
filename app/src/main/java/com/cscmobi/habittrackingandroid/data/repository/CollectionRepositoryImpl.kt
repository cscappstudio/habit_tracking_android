package com.cscmobi.habittrackingandroid.data.repository

import android.content.Context
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import com.cscmobi.habittrackingandroid.data.model.HabitCollection

class CollectionRepositoryImpl(val context: Context) : CollectionRepository {
    private val nightTimeTasks = mutableListOf<Task>(
        Task(
            name = "Practice affirmations",
            ava = context.resources.getResourceEntryName(R.drawable.ic_golf)
        ),
        Task(
            name = "Practice visualization",
            ava = context.resources.getResourceEntryName(R.drawable.ic_golf)
        ),
        Task(
            name = "Practice visualization",
            ava = context.resources.getResourceEntryName(R.drawable.ic_golf)
        ),
        Task(
            name = "Practice visualization",
            ava = context.resources.getResourceEntryName(R.drawable.ic_golf)
        ),
        Task(
            name = "Practice visualization",
            ava = context.resources.getResourceEntryName(R.drawable.ic_golf)
        ),
        Task(
            name = "Practice visualization",
            ava = context.resources.getResourceEntryName(R.drawable.ic_golf)
        ),
        Task(
            name = "Practice visualization",
            ava = context.resources.getResourceEntryName(R.drawable.ic_golf)
        ),
    )

    private var localCollection: MutableList<HabitCollection> = mutableListOf(
        HabitCollection(
            R.drawable.bg_collection1, "Nighttime", nightTimeTasks, R.color.blue
        ),
        HabitCollection(
            R.drawable.bg_collection2, "Morning Routine", nightTimeTasks, R.color.pink
        ),
        HabitCollection(
            R.drawable.bg_collection3, "Heathy lifestyle", nightTimeTasks, R.color.orange
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
            R.drawable.bg_collection8, "Staying fit", nightTimeTasks, R.color.orange
        ),
        HabitCollection(
            R.drawable.bg_collection9, "Hit the Gym", nightTimeTasks, R.color.green
        ),
        HabitCollection(
            R.drawable.bg_collection10, "Self-improvement", nightTimeTasks, R.color.purple
        ),
    )

    override fun getListLocalCollection(): List<HabitCollection> {
        return localCollection
    }

    override fun getListRemoteCollection(): List<HabitCollection> {
        return mutableListOf()
    }
}
package com.cscmobi.habittrackingandroid.data.repository

import android.content.Context
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.thanhlv.model.Task

class HomeRepositoryImpl(private val context: Context) : HomeRepository {
    private val localTask = arrayListOf<Task>(
        Task(name = "1", ava =  context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag1"),
        Task(name= "2", ava =context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag1"),
        Task(name="3",ava = context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag2"),
        Task(name="4", ava =context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag3"),
        Task(name="5",ava = context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag4"),
        Task(name="6",ava = context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag2"),
        Task(name="7", ava =context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag3"),
        Task(name="8", ava =context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag2"),
        Task(name="9",ava = context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag1"),
        Task(name="10", ava =context.resources.getResourceEntryName(R.drawable.ic_golf), tag = "tag1"),
    )

    override fun getListTask(): List<Task> {
        return  localTask
    }

}
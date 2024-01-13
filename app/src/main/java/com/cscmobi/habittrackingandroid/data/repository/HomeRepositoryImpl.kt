package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.Task

class HomeRepositoryImpl : HomeRepository {
    private val localTask = arrayListOf<Task>(
        Task("1", R.drawable.ic_golf, tag = "tag1"),
        Task("2", R.drawable.ic_golf, tag = "tag1"),
        Task("3", R.drawable.ic_golf, tag = "tag2"),
        Task("4", R.drawable.ic_golf, tag = "tag3"),
        Task("5", R.drawable.ic_golf, tag = "tag4"),
        Task("6", R.drawable.ic_golf, tag = "tag2"),
        Task("7", R.drawable.ic_golf, tag = "tag3"),
        Task("8", R.drawable.ic_golf, tag = "tag2"),
        Task("9", R.drawable.ic_golf, tag = "tag1"),
        Task("10", R.drawable.ic_golf, tag = "tag1"),
    )

    override fun getListTask(): List<Task> {
        return  localTask
    }

}
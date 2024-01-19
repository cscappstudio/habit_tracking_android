package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.dto.entities.Task

class HomeRepositoryImpl : HomeRepository {
    private val localTask = arrayListOf<Task>(
        Task(name = "1", ava =  R.drawable.ic_golf, tag = "tag1"),
        Task(name= "2", ava =R.drawable.ic_golf, tag = "tag1"),
        Task(name="3",ava = R.drawable.ic_golf, tag = "tag2"),
        Task(name="4", ava =R.drawable.ic_golf, tag = "tag3"),
        Task(name="5",ava = R.drawable.ic_golf, tag = "tag4"),
        Task(name="6",ava = R.drawable.ic_golf, tag = "tag2"),
        Task(name="7", ava =R.drawable.ic_golf, tag = "tag3"),
        Task(name="8", ava =R.drawable.ic_golf, tag = "tag2"),
        Task(name="9",ava = R.drawable.ic_golf, tag = "tag1"),
        Task(name="10", ava =R.drawable.ic_golf, tag = "tag1"),
    )

    override fun getListTask(): List<Task> {
        return  localTask
    }

}
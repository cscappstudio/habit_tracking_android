package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.data.model.Task

interface HomeRepository {
    fun getListTask() : List<Task>
}
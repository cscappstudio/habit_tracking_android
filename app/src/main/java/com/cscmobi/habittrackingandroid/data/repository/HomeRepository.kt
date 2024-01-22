package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.thanhlv.model.Task


interface HomeRepository {
    fun getListTask() : List<Task>
}
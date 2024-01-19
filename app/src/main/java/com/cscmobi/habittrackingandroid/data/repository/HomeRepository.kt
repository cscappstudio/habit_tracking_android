package com.cscmobi.habittrackingandroid.data.repository

import com.cscmobi.habittrackingandroid.data.dto.entities.Task


interface HomeRepository {
    fun getListTask() : List<Task>
}
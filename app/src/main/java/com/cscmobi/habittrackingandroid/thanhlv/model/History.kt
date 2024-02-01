package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class History(
    @PrimaryKey(true) var id: Int = 0,
    @ColumnInfo(name = "date") var date: Long? = 0,
    @ColumnInfo(name = "taskInDay") var taskInDay: List<TaskInDay> = listOf(),
    @ColumnInfo(name = "progressDay") var progressDay: Int = 0,
    @ColumnInfo(name = "currentStreakDay") var currentStreakDay: Int = 0,
    @ColumnInfo(name = "longStreakDay") var longStreakDay: Int = 0
)
package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import kotlinx.serialization.Serializable

@Serializable
@Entity(indices = [Index(value = ["date"], unique = true)])
data class History(
    @PrimaryKey(true) var id: Long = 0,
    @ColumnInfo(name = "date")  var date: Long? = 0,
    @ColumnInfo(name = "tasksInDay") var tasksInDay: List<TaskInDay> = listOf(),
    @ColumnInfo(name = "progressDay") var progressDay: Int = 0,//3*100/5 -> int
    @ColumnInfo(name = "allTaskPause") var allTaskPause: Boolean = false
) {
    @Ignore
    var currentStreakDay: Int = 0
}
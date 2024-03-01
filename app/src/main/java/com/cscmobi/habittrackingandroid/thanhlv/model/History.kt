package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import kotlinx.serialization.Serializable
import org.checkerframework.common.aliasing.qual.Unique

@Serializable
@Entity(indices = [Index(value = ["date"], unique = true)])
data class History(
    @PrimaryKey(true) var id: Int = 0,
    @ColumnInfo(name = "date")  var date: Long? = 0,
    @ColumnInfo(name = "taskInDay") var taskInDay: List<TaskInDay> = listOf(),
    @ColumnInfo(name = "progressDay") var progressDay: Int = 0 //3*100/5 -> int

//    @ColumnInfo(name = "longStreakDay") var longStreakDay: Int = 0
) {
    @Ignore
    var currentStreakDay: Int = 0
}
package com.cscmobi.habittrackingandroid.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class HabitCollection(
    @PrimaryKey(true) var id: Long = 0,
    @ColumnInfo(name = "image")
    var image: String? = "",
    @ColumnInfo(name = "nameCollection")
    var name: String = "",
    @ColumnInfo(name = "taskCollection")
    var task: List<Task>? = null,
    @ColumnInfo(name = "colorBg")
    var resColorBg: Int? = R.color.blue,

    @Ignore
    var isEdit: Boolean = false
)





data class AlbumCollection(val resDrawable: Int, var isSelected: Boolean = false)

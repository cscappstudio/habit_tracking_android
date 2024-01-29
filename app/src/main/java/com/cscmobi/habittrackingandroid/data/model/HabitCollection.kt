package com.cscmobi.habittrackingandroid.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class HabitCollection(
    @ColumnInfo(name = "image")
    var image: String? = "",
    @PrimaryKey(autoGenerate = false)
    var name: String = "",
    @ColumnInfo(name = "taskCollection")
    var task: List<Task>? = null,
    @ColumnInfo(name = "colorBg")
    var resColorBg: Int? = R.color.blue
)





data class AlbumCollection(val resDrawable: Int, var isSelected: Boolean = false)

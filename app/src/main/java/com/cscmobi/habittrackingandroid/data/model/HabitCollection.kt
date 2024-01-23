package com.cscmobi.habittrackingandroid.data.model

import com.cscmobi.habittrackingandroid.thanhlv.model.Task

data class HabitCollection(
    var image: Int? = null,
    var name: String? = null,
    var task: List<Task>? = null,
    var colorBg: Int? = null
)





data class AlbumCollection(val resDrawable: Int, var isSelected: Boolean = false)

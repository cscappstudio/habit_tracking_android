package com.cscmobi.habittrackingandroid.data.model

data class HabitCollection(
    val image: Int,
    val name: String,
    val task: List<Task>,
    val colorBg: Int
)

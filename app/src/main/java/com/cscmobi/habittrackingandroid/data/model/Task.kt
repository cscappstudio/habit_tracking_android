package com.cscmobi.habittrackingandroid.data.model

import com.cscmobi.habittrackingandroid.R

data class Task (
    val name: String,
    val icon: Int,
    val color: Int = R.color.white,
    val goal: Goal = Goal(),
    val tag: String = ""
)



enum class StatusTask(status: Int) {
    NOTDO(-1),
    DOING(0),
    DONE(1)
}

enum class TypeGoal {
    TypeOneTime(),
    TypebyTimes()
}

data class Goal(
    val statusTask: StatusTask? = null,
    val typeGoal: TypeGoal? = null,
    val progress: Int = 0
)


data class Tag(val name: String, var isSelected: Boolean = false)





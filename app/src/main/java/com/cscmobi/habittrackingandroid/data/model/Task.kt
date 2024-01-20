package com.cscmobi.habittrackingandroid.data.model

import com.cscmobi.habittrackingandroid.R
import java.util.Date

//data class Task (
//    val name: String,
//    val icon: Int,
//    val color: Int = R.color.white,
//    val goal: Goal = Goal(),
//    val tag: String = "",
//    var id: Int = 0,
//
//    )

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
    val unit: String,
    val target: String,
    val period: String,
    val currentProgress: Int,
)

data class CheckList(var status: Boolean, var title: String)

data class TaskRepeat(val type: String, val frequency: Int)
data class Tag(val name: String, var isSelected: Boolean = false)

data class ColorTask(val resId: Int, var isSelected: Boolean = false)

data class History(val time: Date, var pause: Int, var progress: Int)





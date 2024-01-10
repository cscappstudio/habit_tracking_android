package com.cscmobi.habittrackingandroid.data.model

data class Task (
    val name: String,
    val icon: Int,
    val color: Int,
    val goal: Goal,
    val tag: String
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
    val statusTask: StatusTask,
    val typeGoal: TypeGoal,
    val progress: Int
)

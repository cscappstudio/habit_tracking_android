package com.cscmobi.habittrackingandroid.data.model

import androidx.annotation.Keep
import com.cscmobi.habittrackingandroid.thanhlv.database.DateSerializer
import kotlinx.serialization.Contextual
import java.util.Date
import kotlinx.serialization.Serializable

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


@Keep
@Serializable
data class Goal(
    var isOn: Boolean? = false,
    var unit: String? = "",
    var target: Int? = 0,
    var period: String? = "",
    var currentProgress: Int? = 0,
)


@Keep
@Serializable
data class EndDate(
    var isOpen: Boolean? = false,
    @Contextual @Serializable(with = DateSerializer::class) var endDate: Date? = null

)


@Keep
@Serializable
data class RemindTask(
    var isOpen: Boolean? = false,
    var hour: Int? = 0,
    var minute: Int? = 0,
    var unit: String? = "AM"
)


@Keep
@Serializable
data class CheckList(var status: Boolean = false, var title: String)


@Keep
@Serializable
data class TaskRepeat(
    var isOn: Boolean? = false,
    var type: String? = "",
    var frequency: Int? = 0,
    var days: List<Int>? = emptyList()
)

data class Tag(val name: String, var isSelected: Boolean = false)

data class ColorTask(val resId: Int, var isSelected: Boolean = false)


@Serializable
data class TaskInDay(
    var taskId: Int = 0,
    var progress: Int = 0,
    var currentStreak: Int = 0,
    var longStreak: Int = 0
)


@Serializable
data class Tasks(var name: String = "", var id: Int)

@Serializable
data class ChallengeJoinedHistory(var date: Long = 0, var finished: Boolean = false)






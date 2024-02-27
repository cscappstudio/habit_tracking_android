package com.cscmobi.habittrackingandroid.data.model

import androidx.annotation.Keep
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
    var target: Int = 1,
    var period: String? = "",
    var currentProgress: Int = 0,
)


@Keep
@Serializable
data class EndDate(
    var isOpen: Boolean? = false,
     var endDate: Long? = null

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
    var frequency: Int = 1,
    var days: List<Int>? = emptyList()
)

data class Tag(val name: String, var isSelected: Boolean = false)

data class ColorTask(val resId: Int, var isSelected: Boolean = false)


@Keep
@Serializable
data class TaskInDay(
    var taskId: Int = 0,
    var progress: Int = 0,
//    var currentStreak: Int = 0,
//    var longStreak: Int = 0,
    var progressGoal: Int = 0,
//    var target: Int = 1,
  //  var visible:Boolean = true
    )


data class DataTaskHistory(val taskInDay: TaskInDay,val date: Long, var isPause: Boolean = false)


@Serializable
data class TaskInChallenge(
    var name: String = "",
    var description: String = "",
    var icon: String = "",
    var color: String = "",
    var taskNo: Int,
    var id: Int
)

@Serializable
data class ChallengeDays(
    var dayNo: Int = 0,
    var tasks: List<TaskInChallenge> ? = null
)

data class TaskTimelineModel (
    var task: TaskInChallenge,
    var type: Int=0, // 0 = đầu, 1 = giữa, 2 = cuối
    var status: Int=0 // 0 = chưa đến, 1 = done, 2 = chưa done, 3 đã done
)
@Serializable
data class ChallengeJoinedHistory(var date: Long = 0, var finished: Boolean = false)






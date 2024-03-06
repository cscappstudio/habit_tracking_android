package com.cscmobi.habittrackingandroid.data.model

import androidx.annotation.Keep
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.serialization.Serializable

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
    var taskId: Long = 0,
    var progress: Int = 0, //% hoàn thành
    var progressGoal: Int = 0  //thực hiện
)


data class DataTaskHistory(val taskInDay: TaskInDay, val date: Long, var isPause: Boolean = false)


@Serializable
data class TaskInChallenge(
    var name: String = "",
    var description: String = "",
    var icon: String = "",
    var color: String = "",
    var taskNo: Int,
    var dayNo: Int,
    var id: Long? = null,
    var startDate: Long? = null,
    var state: Int = 0 // chưa tham gia, 1 = tham gia đã done, 2= tham gia chưa done, 3 đã done và qua ngày
) {
    fun parserToTask(): Task {
        val task = Task()
        task.name = name
        task.color = color
        task.ava = icon
        this.id = task.id
        return task
    }
}

@Serializable
data class ChallengeDays(
    var dayNo: Int = 0,
    var tasks: List<TaskInChallenge>? = null
)

data class TaskTimelineModel(
    var task: TaskInChallenge,
    var type: Int = 0, // 0 = đầu, 1 = giữa, 2 = cuối
    var status: Int = 0 // 0 = chưa đến, 1 = done, 2 = chưa done, 3 đã done
)

@Serializable
data class ChallengeJoinedHistory(
    var date: Long,
    var state: Int = 0
) // state = 0: tham gia chưa hoàn thành, = 1 tham gia đã hoàn thành, = 2 bị miss task






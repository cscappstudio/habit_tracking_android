package com.cscmobi.habittrackingandroid.thanhlv.model

import com.cscmobi.habittrackingandroid.data.model.TaskInChallenge
import kotlinx.serialization.Serializable
@Serializable
data class CreateTaskChallenge(
    var day: Int,
    var type: Int, // 0 = textday+add; 1 = add; 2 = text + task; 3 = task
    var name: String = "",
    var icon: String = "",
    var color: String = "#B6D6DD"
) {
    fun diff(other: CreateTaskChallenge) : Boolean {
        if (this.name != other.name || this.icon != other.icon || this.color != other.color  ) return true
        return false
    }

    fun parserToTaskInChallenge() : TaskInChallenge{
        return TaskInChallenge(name, "", icon, color, 0, 1, 0)
    }
}
package com.cscmobi.habittrackingandroid.data.model


data class ChallengeHomeItem(
    val idTask:Int = 0,
    val note: String,
    val name: String,
    var stateDone: Boolean = false,
    var srcImage: String
)


data class ChallengeTask(
    val challengeName: String = "",
    var infoTask: InfoTask
)

data class InfoTask(val id: Int, val target:Int ,var status: Boolean = false)
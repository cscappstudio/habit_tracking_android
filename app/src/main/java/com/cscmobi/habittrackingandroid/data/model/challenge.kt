package com.cscmobi.habittrackingandroid.data.model


data class ChallengeHomeItem(
    val idTask:Long = 0,
    val note: String,
    val name: String,
    var stateDone: Boolean = false,
    var srcImage: String
)


data class ChallengeTask(
    val challengeName: String = "",
    var infoTask: InfoTask
)

data class InfoTask(val id: Long, val target:Int ,var status: Boolean = false)
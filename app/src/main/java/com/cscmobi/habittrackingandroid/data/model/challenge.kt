package com.cscmobi.habittrackingandroid.data.model


data class ChallengeHomeItem(
    val idTask:Int = 0,
    val note: String,
    val name: String,
    var stateDone: Boolean = false,
    var srcImage: String
)
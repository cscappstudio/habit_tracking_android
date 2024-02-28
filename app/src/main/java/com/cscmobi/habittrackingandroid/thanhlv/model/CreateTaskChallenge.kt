package com.cscmobi.habittrackingandroid.thanhlv.model

import kotlinx.serialization.Serializable
@Serializable
data class CreateTaskChallenge(
    var day: Int,
    var type: Int, // 0 = textday+add; 1 = add; 2 = text + task; 3 = task
    var name: String = "",
    var icon: String = "",
    var color: String = ""
)
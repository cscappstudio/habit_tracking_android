package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.thanhlv.database.DateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

data class CreateTaskChallenge(
    var day: Int,
    var type: Int, // 0 = textday+add; 1 = add; 2 = text + task; 3 = task
    var name: String = "",
    var icon: String = "",
    var color: String = ""
)
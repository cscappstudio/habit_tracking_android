package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.thanhlv.database.DateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Entity
data class Mood(
    @ColumnInfo(name = "date")  var date: Long = 0,
    @ColumnInfo(name = "state") var state: Int,
    @ColumnInfo(name = "describe") var describe: List<String>,
    @ColumnInfo(name = "becauseOf") var becauseOf: List<String>,
    @ColumnInfo(name = "note") var note: String = ""
) {
    @PrimaryKey(true) var id: Int = 0
}
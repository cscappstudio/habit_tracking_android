package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.data.model.CheckList
import com.cscmobi.habittrackingandroid.data.model.EndDate
import com.cscmobi.habittrackingandroid.data.model.Goal
import com.cscmobi.habittrackingandroid.data.model.RemindTask
import com.cscmobi.habittrackingandroid.data.model.TaskRepeat
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
@Entity
data class Task(
    @PrimaryKey(true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String? = "New Task",
    @ColumnInfo(name = "color") var color: String? = "#B6D6DD",
    @ColumnInfo(name = "ava") var ava: String? ="",
    @ColumnInfo(name = "note") var note: String? = "",
    @ColumnInfo(name = "tag") var tag: String = "",
    @ColumnInfo(name = "collection") var collection: String? = "",
    @ColumnInfo(name = "pause") var pause: Int? = 0, // if pause = -1 mean it pause util turn on again
    @ColumnInfo(name = "challenge") var challenge: String? = "",

    @ColumnInfo(name = "goal")
    var goal: Goal? = Goal(),

    @ColumnInfo(name = "repeat")
    var repeate: TaskRepeat? = TaskRepeat(),

    @ColumnInfo(name = "startDate")   @Contextual var startDate: Date? =null,
    @ColumnInfo(name = "endDate") var endDate: EndDate? = EndDate(),
    @ColumnInfo(name = "remind") var remind: RemindTask? = RemindTask(),
    @ColumnInfo(name = "checklist") var checklist: List<CheckList>? = null,
)

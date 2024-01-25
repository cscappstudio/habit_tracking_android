package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
import com.cscmobi.habittrackingandroid.data.model.Tasks
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Challenge(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "image")
    var image: String = "",
    @ColumnInfo(name = "duration")
    var duration: Int = 7,
    @ColumnInfo(name = "cycle")
    var cycle: Int = 0,
    @ColumnInfo(name = "repeat")
    var repeat: Int = 0,
    @ColumnInfo(name = "joinedHistory")
    var joinedHistory: List<ChallengeJoinedHistory>? = null,
    @ColumnInfo(name = "finish")
    var finish: Boolean = false,

    @ColumnInfo(name = "tasks")
    var tasks: List<Tasks>? = null
) {
    @PrimaryKey(true)
    var id = 0
}



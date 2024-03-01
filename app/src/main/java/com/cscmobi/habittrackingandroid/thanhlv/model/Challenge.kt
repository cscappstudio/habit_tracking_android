package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.data.model.ChallengeDays
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
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
    var repeat: List<Int> = listOf(2,3,4,5,6,7,1), //1 = CN -- Calendar.DAY
    @ColumnInfo(name = "joinedHistory")
    var joinedHistory: ChallengeJoinedHistory?=null,
    @ColumnInfo(name = "finish")
    var finish: Boolean = false,

    @ColumnInfo(name = "tasks")
    var days: List<ChallengeDays> = listOf()
) {
    @PrimaryKey(true)
    var id = 0L
}



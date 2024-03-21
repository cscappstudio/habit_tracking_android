package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cscmobi.habittrackingandroid.data.model.ChallengeDays
import com.cscmobi.habittrackingandroid.data.model.ChallengeJoinedHistory
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Tags(
    @ColumnInfo(name = "name") var name: String = ""
) {
    @PrimaryKey(true)
    var id = 0L
}



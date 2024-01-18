package com.cscmobi.habittrackingandroid.thanhlv.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(var name: String) {
    @PrimaryKey(true)
    var id: Int = 0
}

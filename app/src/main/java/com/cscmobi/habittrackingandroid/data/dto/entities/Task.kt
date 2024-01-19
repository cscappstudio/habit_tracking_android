package com.cscmobi.habittrackingandroid.data.dto.entities

import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.data.model.*
import kotlinx.serialization.json.Json
import java.util.Date

data class Task(
    val id: Int? = 0,
    val name: String? = "",
    val color: Int? = R.color.white,
    val ava: Int? = R.drawable.ic_item_collection2,
    val note: String?= "",
    val tag: String= "",
    val collection: String? = "",
    val goal: Goal? = null,
    val repeate: TaskRepeat? = null,
    val startDate: Date? = Date(),
    val endDate: Date? = Date(),
    val remind: Date? = Date(),
    val checklist:  List<CheckList>? = null,
    val history: List<History>? = null
)


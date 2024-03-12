package com.cscmobi.habittrackingandroid.thanhlv.model

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Build
import java.text.SimpleDateFormat

data class FeelingTagModel(
    var mood: String,
    var describe: String,
    var selected: Boolean = false,
    var type: Int = 1
)
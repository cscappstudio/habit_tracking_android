package com.cscmobi.habittrackingandroid.thanhlv.model

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Build
import java.text.SimpleDateFormat

data class FeelingTagModel(var feeling: String, var selected: Boolean = false)
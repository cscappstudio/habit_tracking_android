package com.cscmobi.habittrackingandroid.thanhlv.model

import com.cscmobi.habittrackingandroid.data.model.Tasks

data class TaskTimelineModel (
    var task: Tasks,
    var type: Int=0, // 0 = đầu, 1 = giữa, 2 = cuối
    var status: Int=0 // 0 = chưa đến, 1 = done, 2 = chưa done, 3 đã done
)
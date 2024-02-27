package com.cscmobi.habittrackingandroid.thanhlv.database

import androidx.room.TypeConverter
import com.cscmobi.habittrackingandroid.data.model.*
import com.cscmobi.habittrackingandroid.thanhlv.model.History
import com.cscmobi.habittrackingandroid.data.model.CheckList
import com.cscmobi.habittrackingandroid.data.model.EndDate
import com.cscmobi.habittrackingandroid.data.model.Goal
import com.cscmobi.habittrackingandroid.data.model.RemindTask
import com.cscmobi.habittrackingandroid.data.model.TaskInDay
import com.cscmobi.habittrackingandroid.data.model.TaskRepeat
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {

    @TypeConverter
    fun fromGoal(goal: Goal): String {
        return Json.encodeToString(goal)
    }

    @TypeConverter
    fun toGoal(goalString: String): Goal {
        return Json.decodeFromString(goalString)
    }

    @TypeConverter
    fun fromRepeat(repeat: TaskRepeat): String {
        return Json.encodeToString(repeat)
    }

    @TypeConverter
    fun toRepeat(repeatString: String): TaskRepeat {
        return Json.decodeFromString(repeatString)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long {
        if (date == null) return Date().time
        return date.time
    }

    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun fromEndDate(endDate: EndDate?): String? {
        // Convert EndDate to String
        return Json.encodeToString(endDate)
    }


    @TypeConverter
    fun toEndDate(endDateString: String?): EndDate? {
        // Convert String to EndDate
        return if (endDateString.isNullOrBlank()) null else Json.decodeFromString(endDateString)
    }

    @TypeConverter
    fun fromRemindTask(remind: RemindTask?): String? {
        // Convert EndDate to String
        return Json.encodeToString(remind)
    }


    @TypeConverter
    fun toRemindTask(remindString: String?): RemindTask? {
        // Convert String to EndDate
        return if (remindString.isNullOrBlank()) null else Json.decodeFromString(remindString)
    }

    @TypeConverter
    fun fromCheckList(checkList: List<CheckList>?): String {
        return Json.encodeToString(checkList ?: emptyList())
    }

    @TypeConverter
    fun toCheckList(checkListString: String): List<CheckList> {
        return Json.decodeFromString(checkListString)
    }

    @TypeConverter
    fun fromRepeatChallenge(history: List<Int>?): String {
        return Json.encodeToString(history ?: emptyList())
    }

    @TypeConverter
    fun toRepeatChallenge(historyString: String): List<Int> {
        return Json.decodeFromString(historyString)
    }

    @TypeConverter
    fun fromChallengeDays(history: List<ChallengeDays>?): String {
        return Json.encodeToString(history ?: emptyList())
    }

    @TypeConverter
    fun toChallengeDays(historyString: String): List<ChallengeDays> {
        return Json.decodeFromString(historyString)
    }


    @TypeConverter
    fun fromTaskInChallenge(tasks: List<Tasks>?): String {
        return Json.encodeToString(tasks ?: emptyList())
    }

    @TypeConverter
    fun toTaskInChallenge(taskInChallenge: String): List<Tasks> {
        return Json.decodeFromString(taskInChallenge)
    }

    @TypeConverter
    fun fromTaskInDayList(taskInDayList: List<TaskInDay>?): String? {
        return if (taskInDayList == null) null else Json.encodeToString(taskInDayList)
    }

    @TypeConverter
    fun toTaskInDayList(taskInDayListJson: String?): List<TaskInDay>? {
        return taskInDayListJson?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromChallengeJoinedHistory(joinedHistory: List<ChallengeJoinedHistory>?): String {
        return Json.encodeToString(joinedHistory ?: emptyList())
    }

    @TypeConverter
    fun toChallengeJoinedHistory(joinedHistory: String): List<ChallengeJoinedHistory> {
        return Json.decodeFromString(joinedHistory)
    }

    @TypeConverter
    fun fromTaskList(taskList: List<Task>?): String? {
        return if (taskList == null) null else Json.encodeToString(taskList)
    }

    @TypeConverter
    fun toTaskList(taskListJson: String?): List<Task>? {
        return taskListJson?.let { Json.decodeFromString(it) }
    }




    @TypeConverter
    fun fromDescribeMood(describeMood: List<String>?): String {
        return Json.encodeToString(describeMood ?: emptyList())
    }

    @TypeConverter
    fun toDescribeMood(describeMood: String): List<String> {
        return Json.decodeFromString(describeMood)
    }

}


@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    private val dateFormat = ThreadLocal<SimpleDateFormat>()

    private fun getDateFormat(): SimpleDateFormat {
        var df = dateFormat.get()
        if (df == null) {
            df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            dateFormat.set(df)
        }
        return df
    }

    override fun deserialize(decoder: Decoder): Date {
        val dateString = decoder.decodeString()
        try {
            return getDateFormat().parse(dateString)
        } catch (e: ParseException) {
            throw IllegalArgumentException("Failed to parse date: $dateString", e)
        }
    }

    override fun serialize(encoder: Encoder, value: Date) {
        val dateString = getDateFormat().format(value)
        encoder.encodeString(dateString)
    }
}
package com.cscmobi.habittrackingandroid.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.cscmobi.habittrackingandroid.AlarmReceiver
import com.cscmobi.habittrackingandroid.thanhlv.model.Task
import java.util.Calendar


/**
 * Created by framgia on 23/05/2017.
 */
object AlarmUtils {
    private var INDEX = 1
    fun create(context: Context,tasks: List<Task>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        for (task  in tasks) {
            intent.putExtra(Constant.KEY_TYPE, INDEX)
            intent.putExtra(Constant.TASKNAME,task.name)

            val pendingIntent =
                PendingIntent.getBroadcast(context, INDEX, intent,     PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            INDEX++
            val calendar = Calendar.getInstance()

            calendar.set(Calendar.HOUR,task.remind!!.hour!!)
            calendar.set(Calendar.MINUTE,task.remind!!.minute!!);
            calendar.set( Calendar.AM_PM, if (task.remind!!.unit == "AM") Calendar.AM else Calendar.PM)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager
                    .setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
            }
        }
    }


    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name = "TaskReminderChannel"
            var description = "Channel for Alarm"

            var importance =  NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("reminder",name,importance)
            channel.description = description

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }



}
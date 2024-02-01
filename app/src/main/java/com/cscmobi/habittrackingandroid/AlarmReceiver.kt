package com.cscmobi.habittrackingandroid

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import com.cscmobi.habittrackingandroid.utils.Constant

class AlarmReceiver : BroadcastReceiver() {

    @RequiresPermission("android.permission.POST_NOTIFICATIONS")
    override fun onReceive(context: Context?, intent: Intent?) {

        if (context != null) {
            val index = intent!!.getIntExtra(Constant.KEY_TYPE, 0)
            var content = ""
            val taskName = intent.getStringExtra(Constant.TASKNAME)
            if (!taskName.isNullOrEmpty()) {
                content = "Let finish your task $taskName"
            }

            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val requestID = System.currentTimeMillis().toInt()
            val contentIntent = PendingIntent
                .getActivity(context, requestID, notificationIntent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)


            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "reminder")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("habit aa")
                .setContentText(content)
//            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//            .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setPriority(6)
                .setContentIntent(contentIntent)


            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(index, builder.build())
        }
    }
}
package com.cscmobi.habittrackingandroid.utils

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity


/**
 * Created by framgia on 23/05/2017.
 */
class SchedulingService : IntentService(SchedulingService::class.java.simpleName) {
    override fun onHandleIntent(intent: Intent?) {
        val index = intent!!.getIntExtra(Constant.KEY_TYPE, 0)
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val requestID = System.currentTimeMillis().toInt()
        val contentIntent = PendingIntent
            .getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this,"1")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("index = $index")
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)
            .setPriority(6)
            .setVibrate(
                longArrayOf(
                    TIME_VIBRATE.toLong(),
                    TIME_VIBRATE.toLong(),
                    TIME_VIBRATE.toLong(),
                    TIME_VIBRATE.toLong(),
                    TIME_VIBRATE.toLong()
                )
            )
            .setContentIntent(contentIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(index, builder.build())
    }

    companion object {
        private const val TIME_VIBRATE = 1000
    }
}
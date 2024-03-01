package com.cscmobi.habittrackingandroid.thanhlv.helper

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.cscmobi.habittrackingandroid.R
import com.cscmobi.habittrackingandroid.presentation.ui.activity.MainActivity
import com.thanhlv.fw.constant.AppConfigs.Companion.HOME_ACTION_FROM_NOTI_PUSH
import com.thanhlv.fw.constant.AppConfigs.Companion.SERVICE_ACTION_START
import kotlin.random.Random

class NotificationHelper(var context: Context) {
    companion object {
        private const val CHANNEL_NOTIFICATION_DEFAULT = "tape_measure_notification_channel"
    }

    enum class NotificationType(val id: Long) {
        FLASH_SALE(15999), OPEN(12399)
    }

    private val applicationContext: Context = context.applicationContext
    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val flagImmutable = PendingIntent.FLAG_IMMUTABLE
    private var currentNotificationType: NotificationType? = null

    fun showNotification(title: String?, message: String?, pendingAction: String?) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.action = pendingAction ?: ""
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0,
            intent,
            flagImmutable
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_NOTIFICATION_DEFAULT)
            .setSmallIcon(R.drawable.ic_icon_add)
            .setLargeIcon(
                AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.ic_icon_add
                )?.toBitmap()
            )
            .setContentTitle(title ?: "")
            .setContentText(message ?: "")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_NOTIFICATION_DEFAULT,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }

    fun createStartAppNotification(){
        val title = applicationContext.getString(R.string.full_app_name)
        val content = "Welcome to Habit"
        showNotification(title, content, SERVICE_ACTION_START)
    }
    fun createRemindFlashSaleNotification(){
        var title = "Welcome to Habit"
        val content = "Welcome to Habit 123"
        when ((1..3).random()) {
            1 -> title = "Welcome to Habit 1"
            2 -> title = "Welcome to Habit 2"
            3 -> title = "Welcome to Habit 3"
        }
        showNotification(title, content, HOME_ACTION_FROM_NOTI_PUSH)
    }

    fun createNotificationChannel() {
        currentNotificationType = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_NOTIFICATION_DEFAULT,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
                    .apply {
                        setSound(null, null)
                        enableLights(false)
                        enableVibration(false)
                        setShowBadge(false)
                    }
            )
        }
    }
}
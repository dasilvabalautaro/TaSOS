package com.globalhiddenodds.tasos.presentation.component

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.globalhiddenodds.tasos.R
import javax.inject.Inject
import android.content.Intent
import android.app.PendingIntent
import android.media.RingtoneManager
import com.globalhiddenodds.tasos.presentation.view.activities.ContactActivity


class Notify @Inject constructor(private val context: Context) {
    private var notificationManager: NotificationManager? = null

    private val channelId = context
            .getString(R.string.default_notification_channel_id)
    private val channelName = context.getString(R.string.channel_name)
    private val notificationId = 201

    init {
        notificationManager =
                context.getSystemService(
                        Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val contentIntent = PendingIntent
            .getActivity(context, 0,
            Intent(context, ContactActivity::class.java), 0)


    fun sendNotification(messageBody: String) {
        val channelId = channelId
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager
                .TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat
                .Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(context.getString(R.string.new_message_notify))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent)

        val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = channelName
            val descriptionText = context.getString(R.string
                    .channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId,
                    name, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }


    fun cancelAllNotification(){
        notificationManager!!.cancelAll()
    }
}
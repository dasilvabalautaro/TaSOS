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
import com.globalhiddenodds.tasos.presentation.view.activities.ContactActivity


class Notify @Inject constructor(private val context: Context) {
    private var notificationManager: NotificationManager? = null

    private val channelId = "com.globalhiddenodds.tasos.news"
    private val channelName = "Notification Tasos"
    private val notificationId = 201
    var builder: NotificationCompat.Builder? = null

    init {
        notificationManager =
                context.getSystemService(
                        Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val contentIntent = PendingIntent.getActivity(context, 0,
            Intent(context, ContactActivity::class.java), 0)

    @Suppress("DEPRECATION")
    private fun buildNotification(title: String, content: String){

        builder = NotificationCompat
                .Builder(context)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setVibrate(longArrayOf(0, 500, 1000))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(contentIntent)
    }

    private fun buildNotificationChannel(title: String, content: String){

        builder = NotificationCompat
                .Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setVibrate(longArrayOf(0, 500, 1000))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent)
    }

    private fun setNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = channelName
            val descriptionText = context.getString(R.string
                    .channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId,
                    name, importance).apply {
                description = descriptionText
            }

            notificationManager!!.createNotificationChannel(channel)
        }
    }

    fun defineNotification(title: String, content: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setNotificationChannel()
            buildNotificationChannel(title, content)
        }else{
            buildNotification(title, content)
        }
    }

    fun showNotification(){
        notificationManager!!.notify(notificationId, builder!!.build())

    }

    fun cancelAllNotification(){
        notificationManager!!.cancelAll()
    }
}
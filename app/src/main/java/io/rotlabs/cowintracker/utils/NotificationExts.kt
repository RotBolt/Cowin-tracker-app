package io.rotlabs.cowintracker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat


/**
 *
 *      {
"district_id": 276,
"district_name": "Bangalore Rural"
},
{
"district_id": 265,
"district_name": "Bangalore Urban"
},
{
"district_id": 294,
"district_name": "BBMP"
},
 *
 */

const val CHANNEL_ID_FOREGROUND_SERVICE_NOTIFICATION = "io.rotlabs.cowintracker.notification"
const val VACCINE_INFO_NOTIFICATION_ID = "io.rotlabs.cowintracker.vaccine.info.notification"


fun Context.trackingNotification(): Notification? {

    val builder = NotificationCompat.Builder(this)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        builder.setChannelId(
            CHANNEL_ID_FOREGROUND_SERVICE_NOTIFICATION
        )

        val channelName = "My Background Service"
        val chan = NotificationChannel(
            CHANNEL_ID_FOREGROUND_SERVICE_NOTIFICATION,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)
    }
    return builder
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Tracking Vaccines")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(longArrayOf(1, 2, 1, 2))
        .build()
}

fun Context.vaccineInfoNotification(centreNames: List<String>): Notification? {

    val builder = NotificationCompat.Builder(this)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        builder.setChannelId(
            VACCINE_INFO_NOTIFICATION_ID
        )

        val channelName = "Vaccine Info"
        val chan = NotificationChannel(
            VACCINE_INFO_NOTIFICATION_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)
    }
    return builder
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("18+ Vaccine found")
        .setContentText(centreNames.joinToString("\n"))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(longArrayOf(1, 2, 1, 2))
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(centreNames.joinToString("\n"))
        )

        .build()
}


fun Context.getNotificationManager(): NotificationManager {
    return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

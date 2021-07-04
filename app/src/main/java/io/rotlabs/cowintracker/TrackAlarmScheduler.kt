package io.rotlabs.cowintracker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import io.rotlabs.cowintracker.utils.scheduleExactAndAllowWhileIdle

object TrackAlarmScheduler {

    private const val REPEAT_INTERVAL = 2 * 60 * 1000

    fun scheduleTrackService(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = getTrackScheduleReceiverPendingIntent(context)
        alarmManager.scheduleExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + REPEAT_INTERVAL,
            pi
        )
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = getTrackScheduleReceiverPendingIntent(context)
        alarmManager.cancel(pi)

    }

    private fun getTrackScheduleReceiverPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, TrackScheduleReceiver::class.java)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }
}
package io.rotlabs.cowintracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.legacy.content.WakefulBroadcastReceiver

class TrackScheduleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        TrackService.trackVaccines(context)
    }
}
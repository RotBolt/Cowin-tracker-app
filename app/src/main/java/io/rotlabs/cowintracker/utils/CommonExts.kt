package io.rotlabs.cowintracker.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

fun Context.startServiceInForeground(intent: Intent) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}

fun Context.isNetworkConnected(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isConnected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        capabilities?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || it.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            )
        }
    } else {
        connectivityManager.activeNetworkInfo?.isConnected
    }
    return isConnected ?: false
}

fun AlarmManager.scheduleExactAndAllowWhileIdle(
    type: Int,
    triggerAtMillis: Long,
    operation: PendingIntent
) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        setExactAndAllowWhileIdle(type, triggerAtMillis, operation)
    } else {
        setExact(type, triggerAtMillis, operation)
    }
}


fun getCurrentDate(): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
    }

    val day = calendar.get(Calendar.DATE)
    val month = calendar.get(Calendar.MONTH) + 1
    val year = calendar.get(Calendar.YEAR)

    val date = "${day.toString().padStart(2, '0')}-${month.toString().padStart(2, '0')}-$year"
    return date
}
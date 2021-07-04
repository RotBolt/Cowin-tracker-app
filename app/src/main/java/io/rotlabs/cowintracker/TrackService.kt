package io.rotlabs.cowintracker

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import io.rotlabs.cowintracker.data.CenterNotifiedPrefs
import io.rotlabs.cowintracker.data.model.Center
import io.rotlabs.cowintracker.data.remote.Networking
import io.rotlabs.cowintracker.utils.getCurrentDate
import io.rotlabs.cowintracker.utils.isNetworkConnected
import io.rotlabs.cowintracker.utils.startServiceInForeground


class TrackService : Service() {

    private lateinit var wl: WakeLock
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler
    private lateinit var trackRunnable: Runnable
    private lateinit var centrePrefs: CenterNotifiedPrefs

    private fun scheduleNext() {
        handler.postDelayed(trackRunnable, 2 * 60 * 1000)
    }

    companion object {

        private val TRACK_NOTIF_ID = 1

        @JvmStatic
        fun trackVaccines(context: Context) {
            val intent = Intent(context, TrackService::class.java)
            context.startServiceInForeground(intent)
        }

        @JvmStatic
        fun stopTracking(context: Context) {
            val intent = Intent(context, TrackService::class.java)
            context.stopService(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notifyForegroundProcessing()
        handler.post(trackRunnable)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val pm: PowerManager = baseContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "cowintracker:trackWakeLock");
        wl.acquire()
        handlerThread = HandlerThread("hola")
        handlerThread.isDaemon = true
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        centrePrefs = CenterNotifiedPrefs(baseContext)
        trackRunnable = Runnable {
            if (baseContext.isNetworkConnected()) {
                checkAndNotify()
            }
            scheduleNext()
        }
    }

    @Synchronized
    private fun checkAndNotify(){
        val list = checkVaccines()
        val currentTimeStamp = System.currentTimeMillis()

        if (list.isNotEmpty()) {
            val centreNames = list.map {
                "${it.name}:${it.districtName}"
            }.toMutableList()
            Log.d("PUI", "18+ List $centreNames")
            val timeStampedList = centrePrefs.getListCentresTimeStampedList()
            Log.d("PUI", "18+ List timestamped $timeStampedList")
            val toRemoveList = mutableListOf<String>()
            timeStampedList.forEach { timeStampedCentre ->
                centreNames.forEach { centreName ->
                    if (timeStampedCentre.contains(centreName)) {
                        val timeStamp = timeStampedCentre.split(":")[2].toLong()
                        if (currentTimeStamp - timeStamp < 45 * 60 * 1000) {
                            toRemoveList.add(centreName)
                        }
                    }
                }
            }
            centreNames.removeAll(toRemoveList)
            Log.d("PUI", "18+ List after $centreNames")
            if (centreNames.isNotEmpty()) {
                centrePrefs.appendList(centreNames)
                val nm = getNotificationManager()
                nm.notify(21, vaccineInfoNotification(centreNames))
            }
        }
    }


    private fun checkVaccines(): List<Center> {
        val vaccineApi = Networking.getVaccineCalendarApi()
        val mutableList = mutableListOf<Center>()
        val currentDate = getCurrentDate().trim()
        Log.d("PUI", "Current Date $currentDate")
        // BBMP
        val bbmpCall = vaccineApi.getVaccineAppointmentsByCalendar("294", currentDate).execute()
        val bbmpList = bbmpCall.body()
        Log.d("PUI", bbmpList.toString())
        Log.d("PUI", "ERROR ${bbmpCall.errorBody()?.string()}")
        Log.d("PUI", "Code ${bbmpCall.code()}")
        bbmpList?.let { mutableList.addAll(it.centers) }

        // BLR urban
        val blrUrbanList =
            vaccineApi.getVaccineAppointmentsByCalendar("265", currentDate).execute().body()
        blrUrbanList?.let { mutableList.addAll(it.centers) }

        // BLR rural
        val blrRuralList =
            vaccineApi.getVaccineAppointmentsByCalendar("276", currentDate).execute().body()
        blrRuralList?.let { mutableList.addAll(it.centers) }

        val _18PlusList = mutableList.filter { center ->
            val _18PlusSessions = center.sessions.filter {
                it.minAgeLimit == 18 && it.availableCapacity > 0
            }
            _18PlusSessions.isNotEmpty()
        }

        return _18PlusList

    }

    override fun onDestroy() {
        super.onDestroy()
        wl.release()
        handlerThread.quit()
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun notifyForegroundProcessing() {
        startForeground(TRACK_NOTIF_ID, trackingNotification())
    }
}
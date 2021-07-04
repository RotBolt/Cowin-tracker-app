package io.rotlabs.cowintracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var pm: PowerManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val btnTrack = findViewById<Button>(R.id.btnTrack)
        val btnStopTrack = findViewById<Button>(R.id.btnStopTrack)
        btnTrack.setOnClickListener {
            TrackService.trackVaccines(this)
        }

        btnStopTrack.setOnClickListener {
            TrackService.stopTracking(this)
        }
    }

    override fun onStart() {
        super.onStart()
        checkAndRequestBatteryOptimizations()
    }

    @SuppressLint("BatteryLife")
    private fun checkAndRequestBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName = packageName
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }
}
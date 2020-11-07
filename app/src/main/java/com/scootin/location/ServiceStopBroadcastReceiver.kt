package com.scootin.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.birjuvachhani.locus.Locus
import timber.log.Timber

internal class ServiceStopBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.i("Received broadcast to stop location updates")
        Locus.stopLocationUpdates()
        context.stopService(Intent(context, LocationService::class.java))
    }
}
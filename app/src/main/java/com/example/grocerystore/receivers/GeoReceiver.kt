package com.example.grocerystore.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeoReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geoFenceEvent = GeofencingEvent.fromIntent(intent)
        if(geoFenceEvent !=null){
            for (geofence in geoFenceEvent.triggeringGeofences!!){
                if(geoFenceEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
                    Toast.makeText(context, "We entered ${geofence.requestId}", Toast.LENGTH_SHORT).show()
                }
                else if(geoFenceEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
                    Toast.makeText(context, "We exited ${geofence.requestId}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Log.e("GeoError", "GeofencingEvent is null")
        }
    }
}
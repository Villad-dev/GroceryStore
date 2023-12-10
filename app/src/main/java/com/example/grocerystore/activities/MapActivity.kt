package com.example.grocerystore.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.grocerystore.activities.ui.theme.GroceryStoreTheme
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class MapActivity : ComponentActivity() {

    companion object{
        var id = 0
    }

    private lateinit var geoClient: GeofencingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geoClient = LocationServices.getGeofencingClient(this)
        setContent {
            GroceryStoreTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold { scaffoldPadding ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                                .padding(scaffoldPadding)
                        ) {
                        ContentComp(applicationContext, geoClient)
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("MutableImplicitPendingIntent")
@Composable
fun ContentComp(context: Context, geofencingClient: GeofencingClient){
    var location by remember {
        mutableStateOf("")
    }
    
    Text("The current location is $location")

    OutlinedButton(onClick = { /*TODO*/ }) {
        Text(text = "Get location")
        
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        Toast.makeText(context, "Permission is missing", Toast.LENGTH_LONG).show()
        //return
    }
    LocationServices.getFusedLocationProviderClient(context).lastLocation
        .addOnSuccessListener {
            location = "latitude = ${it.latitude}, longitude=${it.longitude}"
            val geofence = Geofence.Builder()
                .setCircularRegion(it.latitude,
                    it.longitude,
                    50F)
                .setExpirationDuration(60*60*1000)
                .setRequestId("GEO ${MapActivity.id++}")
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()

            val request = GeofencingRequest.Builder()
                .addGeofence(geofence)
                .build()

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                MapActivity.id,
                Intent(),
                PendingIntent.FLAG_MUTABLE
            )
            geofencingClient.addGeofences(request, pendingIntent)
                .addOnSuccessListener {
                    Toast.makeText(context, "Geofence: ${MapActivity.id} added successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(context, "Geofence: ${MapActivity.id} ERROR!", Toast.LENGTH_SHORT).show()
                }
        }
        .addOnFailureListener{
            location = "NOT SET"
        }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GroceryStoreTheme {
    //    ContentComp()
    }
}
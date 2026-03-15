package com.example.soundconnect.ui.map

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundconnect.R
import com.example.soundconnect.data.local.MusicTagEntity
import com.example.soundconnect.data.local.TrackDao
import com.example.soundconnect.domain.repository.TrackRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class MapViewModel @Inject constructor(
    private val dao: TrackDao,
    private val repository: TrackRepository,
    @ApplicationContext private val context: Context
) : ViewModel(), SensorEventListener {

    val musicTags = dao.getAllMusicTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var recommendedTrack by mutableStateOf<String?>(null)

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "music_proximity_channel"
    private val notifiedTags = mutableSetOf<String>()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                checkProximity(location)
            }
        }
    }

    init {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Proximidad Musical",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationTracking() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun checkProximity(location: Location) {
        musicTags.value.forEach { tag ->
            val results = FloatArray(1)
            Location.distanceBetween(location.latitude, location.longitude, tag.latitude, tag.longitude, results)
            val distance = results[0]

            if (distance < 100f && !notifiedTags.contains(tag.trackId)) {
                sendNotification(tag)
                notifiedTags.add(tag.trackId)
            } else if (distance >= 100f) {
                notifiedTags.remove(tag.trackId)
            }
        }
    }

    private fun sendNotification(tag: MusicTagEntity) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¡Music Tag cerca de ti!")
            .setContentText("Alguien escuchó ${tag.trackName} de ${tag.artistName} justo aquí.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(tag.trackId.hashCode(), notification)
    }

    fun saveMusicTag(lat: Double, lng: Double) {
        viewModelScope.launch {
            val result = repository.searchTracks("Top Hits")
            if (result.isSuccess) {
                val tracks = result.getOrNull()
                if (!tracks.isNullOrEmpty()) {
                    val track = tracks.random()
                    dao.insertMusicTag(MusicTagEntity(
                        trackId = track.id.toString(),
                        trackName = track.name,
                        artistName = track.artist,
                        latitude = lat,
                        longitude = lng
                    ))
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
            if (acceleration > 12) {
                recommendedTrack = "Recomendación: Daft Punk - One More Time"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
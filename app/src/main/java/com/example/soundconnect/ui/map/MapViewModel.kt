package com.example.soundconnect.ui.map

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundconnect.data.local.MusicTagEntity
import com.example.soundconnect.data.local.TrackDao
import com.example.soundconnect.domain.repository.TrackRepository
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
    @ApplicationContext context: Context
) : ViewModel(), SensorEventListener {

    val musicTags = dao.getAllMusicTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultAdapter()?.let { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    var recommendedTrack by mutableStateOf<String?>(null)

    init {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun saveMusicTag(lat: Double, lng: Double) {
        viewModelScope.launch {
            repository.searchTracks("Top Hits").onSuccess { tracks ->
                val track = tracks.randomOrNull() ?: return@onSuccess
                dao.insertMusicTag(MusicTagEntity(
                    trackId = track.id,
                    trackName = track.name,
                    artistName = track.artist,
                    latitude = lat,
                    longitude = lng
                ))
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
            if (acceleration > 12) { // Agitar detectado
                recommendedTrack = "Recomendación: Daft Punk - One More Time"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}

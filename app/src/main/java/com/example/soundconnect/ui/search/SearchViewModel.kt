package com.example.soundconnect.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundconnect.domain.model.Track
import com.example.soundconnect.domain.repository.TrackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: TrackRepository
) : ViewModel() {
    var query by mutableStateOf("")
    var tracks by mutableStateOf<List<Track>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun search() {
        if (query.isBlank()) return
        viewModelScope.launch {
            isLoading = true
            repository.searchTracks(query)
                .onSuccess { tracks = it; error = null }
                .onFailure { error = it.message }
            isLoading = false
        }
    }

    fun toggleFavorite(track: Track) {
        viewModelScope.launch {
            repository.toggleFavorite(track)
            tracks = tracks.map {
                if (it.id == track.id) it.copy(isFavorite = !it.isFavorite) else it
            }
        }
    }
}

package com.example.soundconnect.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soundconnect.domain.model.Track

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = viewModel.query,
            onValueChange = { viewModel.query = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar música...") },
            trailingIcon = {
                IconButton(onClick = { viewModel.search() }) {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(viewModel.tracks) { track ->
                    TrackItem(track = track, onFavoriteClick = { viewModel.toggleFavorite(track) })
                }
            }
        }
        viewModel.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

@Composable
fun TrackItem(track: Track, onFavoriteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = track.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(track.name, style = MaterialTheme.typography.titleMedium)
                Text(track.artist, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (track.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}

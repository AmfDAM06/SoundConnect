package com.example.soundconnect.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.soundconnect.domain.repository.AuthRepository

@Composable
fun ProfileScreen(authRepository: AuthRepository) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val user = authRepository.currentUser

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil de Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        AsyncImage(
            model = imageUri ?: "https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y",
            contentDescription = null,
            modifier = Modifier.size(120.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = user?.email ?: "Usuario no identificado")

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Cambiar Foto de Perfil")
        }
    }
}
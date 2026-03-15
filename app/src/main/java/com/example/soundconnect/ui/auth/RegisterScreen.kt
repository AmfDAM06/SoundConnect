package com.example.soundconnect.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = viewModel.password, onValueChange = { viewModel.password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))
        if (viewModel.isLoading) CircularProgressIndicator()
        else {
            Button(onClick = { viewModel.register(onRegisterSuccess) }) { Text("Registrarse") }
            TextButton(onClick = onNavigateToLogin) { Text("¿Ya tienes cuenta? Inicia sesión") }
        }
        viewModel.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

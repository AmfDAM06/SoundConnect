package com.example.soundconnect.ui.auth

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    onGoogleLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = viewModel.password, onValueChange = { viewModel.password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = { viewModel.login(onLoginSuccess) }, modifier = Modifier.fillMaxWidth()) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onGoogleLoginClick, modifier = Modifier.fillMaxWidth()) {
                Text("Login con Google")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    activity?.let {
                        val provider = OAuthProvider.newBuilder("facebook.com")
                        FirebaseAuth.getInstance()
                            .startActivityForSignInWithProvider(it, provider.build())
                            .addOnSuccessListener {
                                onLoginSuccess()
                            }
                            .addOnFailureListener { e ->
                                viewModel.error = e.message
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login con Facebook")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToRegister) {
                Text("No tienes cuenta? Regístrate")
            }
        }
        viewModel.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}
package com.example.soundconnect.ui.auth

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    onGoogleLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val callbackManager = remember { CallbackManager.Factory.create() }

    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                viewModel.loginWithCredential(credential, onLoginSuccess)
            }
            override fun onCancel() {}
            override fun onError(error: FacebookException) {
                viewModel.error = error.message
            }
        })
        onDispose {
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }

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
                    LoginManager.getInstance().logIn(
                        context as ComponentActivity,
                        callbackManager,
                        listOf("email", "public_profile")
                    )
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
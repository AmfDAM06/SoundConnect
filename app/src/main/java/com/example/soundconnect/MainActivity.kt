package com.example.soundconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.soundconnect.ui.auth.AuthViewModel
import com.example.soundconnect.ui.auth.LoginScreen
import com.example.soundconnect.ui.auth.RegisterScreen
import com.example.soundconnect.ui.chat.ChatScreen
import com.example.soundconnect.ui.chat.ChatViewModel
import com.example.soundconnect.ui.navigation.Screen
import com.example.soundconnect.ui.search.SearchScreen
import com.example.soundconnect.ui.search.SearchViewModel
import com.example.soundconnect.ui.map.MapScreen
import com.example.soundconnect.ui.map.MapViewModel
import com.example.soundconnect.ui.theme.SoundConnectTheme
import com.example.soundconnect.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            SoundConnectTheme(darkTheme = themeViewModel.isDarkMode) {
                SoundConnectNavGraph()
            }
        }
    }
}

@Composable
fun SoundConnectNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = if (authViewModel.isAuth) "main" else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onGoogleLoginClick = { /* Implementar login Google */ }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate("main") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val items = listOf(
                    Triple(Screen.Search, "Buscar", Icons.Default.Search),
                    Triple(Screen.Chat, "Chat", Icons.AutoMirrored.Filled.Chat),
                    Triple(Screen.Map, "Mapa", Icons.Default.Map)
                )

                items.forEach { (screen, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Search.route, Modifier.padding(innerPadding)) {
            composable(Screen.Search.route) { 
                val viewModel: SearchViewModel = hiltViewModel()
                SearchScreen(viewModel) 
            }
            composable(Screen.Chat.route) { 
                val viewModel: ChatViewModel = hiltViewModel()
                ChatScreen(viewModel) 
            }
            composable(Screen.Map.route) { 
                val viewModel: MapViewModel = hiltViewModel()
                MapScreen(viewModel) 
            }
        }
    }
}

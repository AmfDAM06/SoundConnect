package com.example.soundconnect.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Search : Screen("search")
    data object Favorites : Screen("favorites")
    data object Chat : Screen("chat")
    data object Map : Screen("map")
    data object Profile : Screen("profile")
}
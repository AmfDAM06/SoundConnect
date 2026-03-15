package com.example.soundconnect.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Chat : Screen("chat")
    object Map : Screen("map")
}
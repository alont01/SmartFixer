package com.example.smartfixer.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Results : Screen("results")
    object Profile : Screen("profile")
    object PastFixes : Screen("past_fixes")
    object ContactPro : Screen("contact_pro")
}

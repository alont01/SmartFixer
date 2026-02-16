package com.example.smartfixer.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Results : Screen("results")
    object Profile : Screen("profile")
    object PastFixes : Screen("past_fixes")
    object ContactPro : Screen("contact_pro")
    object PastFixDetail : Screen("past_fix_detail/{fixId}") {
        fun createRoute(fixId: Long) = "past_fix_detail/$fixId"
    }
    object ExpertOnboarding : Screen("expert_onboarding")
}

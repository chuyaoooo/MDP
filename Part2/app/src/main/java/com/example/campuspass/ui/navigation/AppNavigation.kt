package com.example.campuspass.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campuspass.ui.components.BottomBarItem
import com.example.campuspass.ui.components.CampusBottomBar
import com.example.campuspass.ui.components.PlaceholderScreen
import com.example.campuspass.ui.login.LoginRoute
import com.example.campuspass.ui.student.marketplace.MarketplaceRoute

/** Route names for every destination in the app. */
object Routes {
    const val LOGIN = "login"
    const val STUDENT = "student"

    // Student bottom bar tabs
    const val HOME = "home"
    const val BOOKINGS = "bookings"
    const val SCAN = "scan"
    const val POINTS = "points"
    const val LEADERBOARD = "leaderboard"
    const val PROFILE = "profile"
}

/** The six student tabs, in the order shown in Figma. */
val studentTabs = listOf(
    BottomBarItem(Routes.HOME, "Home"),
    BottomBarItem(Routes.BOOKINGS, "Bookings"),
    BottomBarItem(Routes.SCAN, "Scan"),
    BottomBarItem(Routes.POINTS, "Points"),
    BottomBarItem(Routes.LEADERBOARD, "Leaderboard"),
    BottomBarItem(Routes.PROFILE, "Profile")
)

/** Top level: Login first, then the student area. Club screens will be added here later. */
@Composable
fun CampusPassApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginRoute(onLoginSuccess = {
                navController.navigate(Routes.STUDENT) {
                    // Pressing back after logging in should not return to the login form
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            })
        }
        composable(Routes.STUDENT) { StudentShell() }
    }
}

/** Student area: the bottom bar plus whichever tab is selected. */
@Composable
fun StudentShell() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            CampusBottomBar(
                items = studentTabs,
                selectedRoute = currentRoute,
                onSelect = { tab ->
                    navController.navigate(tab.route) {
                        // Keep one copy of each tab and remember its scroll position
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                MarketplaceRoute(onEventClick = { /* TODO: open event details (next screen) */ })
            }
            composable(Routes.BOOKINGS) { PlaceholderScreen("My Bookings") }
            composable(Routes.SCAN) { PlaceholderScreen("Check in") }
            composable(Routes.POINTS) { PlaceholderScreen("Points") }
            composable(Routes.LEADERBOARD) { PlaceholderScreen("Leaderboard") }
            composable(Routes.PROFILE) { PlaceholderScreen("Profile") }
        }
    }
}

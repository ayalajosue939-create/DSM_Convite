package com.udb.convite.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.udb.convite.ui.auth.AuthScreen
import com.udb.convite.ui.calendar.CalendarScreen
import com.udb.convite.ui.create.CreateEventScreen
import com.udb.convite.ui.detail.EventDetailScreen
import com.udb.convite.ui.home.HomeScreen
import com.udb.convite.ui.profile.ProfileScreen
import com.udb.convite.ui.reminders.RemindersScreen

@Composable
fun ConviteNavGraph() {
    val navController = rememberNavController()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) "home" else "auth"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToCreate = { navController.navigate("create_event") },
                onNavigateToDetail = { eventId -> navController.navigate("event_detail/$eventId") },
                onNavigateToCalendar = { navController.navigate("calendar") },
                onNavigateToReminders = { navController.navigate("reminders") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }
        composable("create_event") {
            CreateEventScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "event_detail/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EventDetailScreen(
                eventId = eventId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("reminders") {
            RemindersScreen(
                onNavigateHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateCalendar = { navController.navigate("calendar") },
                onNavigateProfile = { navController.navigate("profile") }
            )
        }
        composable("calendar") {
            CalendarScreen(
                onNavigateHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateReminders = { navController.navigate("reminders") },
                onNavigateProfile = { navController.navigate("profile") }
            )
        }
        composable("profile") {
            ProfileScreen(
                onNavigateHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                },
                onNavigateCalendar = { navController.navigate("calendar") },
                onNavigateReminders = { navController.navigate("reminders") },
                onSignOut = {
                    navController.navigate("auth") {
                        popUpTo("profile") { inclusive = true }
                    }
                }
            )
        }
    }
}

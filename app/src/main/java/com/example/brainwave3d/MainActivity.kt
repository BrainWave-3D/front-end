package com.example.brainwave3d

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brainwave3d.di.AuthState
import com.example.brainwave3d.di.AuthStateManager
import com.example.brainwave3d.ui.navigation.Screen
import com.example.brainwave3d.ui.presentation.auth_screen.login_screen.LoginScreen
import com.example.brainwave3d.ui.presentation.auth_screen.signup_screen.SignUpScreen
import com.example.brainwave3d.ui.presentation.auth_screen.user_details_screen.UserDetailsScreen
import com.example.brainwave3d.ui.presentation.home_screen.HomeScreen
import com.example.brainwave3d.ui.presentation.profile_screen.ProfileScreen
import com.example.brainwave3d.ui.theme.BrainWave3DTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authStateManager: AuthStateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            BrainWave3DTheme {
                val navController = rememberNavController()
                var isAuthenticated by remember { mutableStateOf<Boolean?>(null) }

                // Listen to auth state changes
                LaunchedEffect(Unit) {
                    authStateManager.checkAuthStatus()

                    authStateManager.authState.collect { state ->
                        when (state) {
                            is AuthState.Authenticated -> {
                                isAuthenticated = true
                            }
                            is AuthState.Unauthenticated -> {
                                isAuthenticated = false
                                // Navigate to login and clear back stack
                                navController.navigate(Screen.LoginScreen) {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                // Set start destination based on auth state
                val startDestination = when (isAuthenticated) {
                    true -> Screen.HomeScreen
                    false -> Screen.LoginScreen
                    null -> Screen.LoginScreen // Default to login while checking
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        // ========== Auth Screens ==========

                        composable<Screen.LoginScreen> {
                            LoginScreen(
                                onLoginSuccess = {
                                    // Navigate to home and clear auth screens from back stack
                                    navController.navigate(Screen.HomeScreen) {
                                        popUpTo(Screen.LoginScreen) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                onNavigateToSignUp = {
                                    navController.navigate(Screen.SignUpScreen)
                                }
                            )
                        }

                        composable<Screen.SignUpScreen> {
                            SignUpScreen(
                                onSignUpSuccess = {
                                    // Navigate to user details or home
                                    navController.navigate(Screen.UserDetailsScreen) {
                                        popUpTo(Screen.SignUpScreen) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // ========== Main App Screens ==========

                        composable<Screen.HomeScreen> {
                            HomeScreen(
                                onProfileClick = {
                                    navController.navigate(Screen.ProfileScreen)
                                },
//                                onNavigateToDetails = {
//                                    navController.navigate(Screen.UserDetailsScreen)
//                                }
                            )
                        }

                        composable<Screen.UserDetailsScreen> {
                            UserDetailsScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onSubmit = { patientDetails ->
                                    // Save patient details and navigate back to home
                                    navController.navigate(Screen.HomeScreen) {
                                        popUpTo(Screen.HomeScreen) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        composable<Screen.ProfileScreen> {
                            ProfileScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onLogout = {
                                    // Trigger logout through AuthStateManager
                                    lifecycleScope.launch {
                                        authStateManager.setUnauthenticated()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
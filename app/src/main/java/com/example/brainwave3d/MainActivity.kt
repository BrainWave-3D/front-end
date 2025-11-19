package com.example.brainwave3d

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brainwave3d.ui.navigation.Screen
import com.example.brainwave3d.ui.presentation.auth_screen.login_screen.LoginScreen
import com.example.brainwave3d.ui.presentation.auth_screen.signup_screen.SignUpScreen
import com.example.brainwave3d.ui.presentation.auth_screen.user_details_screen.UserDetailsScreen
import com.example.brainwave3d.ui.presentation.home_screen.HomeScreen
import com.example.brainwave3d.ui.presentation.profile_screen.ProfileScreen
import com.example.brainwave3d.ui.theme.BrainWave3DTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrainWave3DTheme {

                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )

//                    LoginScreen(
//                        onLoginSuccess = {},
//                        onNavigateToSignUp = {}
//                    )

//                    SignUpScreen(
//                        onNavigateToLogin = {},
//                        onSignUpSuccess = {}
//                    )
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen
                    ) {
                        composable<Screen.HomeScreen> {
                            HomeScreen(
                                onProfileClick = {
                                    navController.navigate(Screen.ProfileScreen)
                                }
                            )
                        }

                        composable<Screen.SignUpScreen> {
                            SignUpScreen(
                                onNavigateToLogin = {
                                    navController.navigate(Screen.LoginScreen)
                                },
                                onSignUpSuccess = {
                                    navController.navigate(Screen.HomeScreen)
                                }
                            )
                        }

                        composable<Screen.LoginScreen> {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(Screen.LoginScreen)
                                },
                                onNavigateToSignUp = {
                                    navController.navigate(Screen.SignUpScreen)
                                }
                            )
                        }

                        composable<Screen.UserDetailsScreen> {
                            UserDetailsScreen(
                                onNavigateBack = {},
                                onSubmit = {}
                            )
                        }

                        composable<Screen.ProfileScreen> {
                            ProfileScreen(
                                onNavigateBack = {}
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BrainWave3DTheme {
        Greeting("Android")
    }
}
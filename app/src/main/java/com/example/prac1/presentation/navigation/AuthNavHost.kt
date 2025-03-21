package com.example.prac1.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.prac1.presentation.composable.AuthScreen
import com.example.prac1.presentation.composable.RegisterScreen
import com.example.prac1.presentation.viewmodel.AuthViewModel

/**
 * Composable function for handling authentication-related navigation.
 *
 * @param navController Navigation controller managing screen transitions.
 * @param authViewModel ViewModel for handling authentication logic.
 *
 * @author Sofia Bakalskaya
 */
@Composable
fun AuthNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Auth.route
    ) {
        /**
         * Authentication screen.
         * Provides user login functionality and navigation to the registration screen.
         */
        composable(Screens.Auth.route) {
            AuthScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screens.Register.route)
                }
            )
        }
        /**
         * Registration screen.
         * Allows users to sign up and navigate back to the authentication screen.
         */
        composable(Screens.Register.route) {
            RegisterScreen(
                onNavigateToAuth = {
                    navController.popBackStack(Screens.Auth.route, inclusive = false)
                },
                authViewModel = authViewModel
            )
        }
    }
}
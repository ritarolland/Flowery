package com.example.prac1.presentation.composable

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.prac1.presentation.navigation.AppNavHost
import com.example.prac1.presentation.navigation.AuthNavHost
import com.example.prac1.presentation.navigation.BottomNavigationBar
import com.example.prac1.presentation.viewmodel.AllOrdersViewModel
import com.example.prac1.presentation.viewmodel.AuthViewModel
import com.example.prac1.presentation.viewmodel.CartViewModel
import com.example.prac1.presentation.viewmodel.CatalogViewModel
import com.example.prac1.presentation.viewmodel.DetailsViewModel
import com.example.prac1.presentation.viewmodel.FavouritesViewModel
import com.example.prac1.presentation.viewmodel.OrderViewModel
import com.example.prac1.presentation.viewmodel.ProfileViewModel

/**
 * Main application screen composable that handles authentication state
 * and displays appropriate navigation hosts
 *
 * @param authViewModel ViewModel handling authentication state
 * @param navController Navigation controller for app navigation
 * @param catalogViewModel ViewModel for catalog functionality
 * @param detailsViewModel ViewModel for product details
 * @param cartViewModel ViewModel for cart operations
 * @param profileViewModel ViewModel for user profile
 * @param favouritesViewModel ViewModel for favorites
 * @param allOrdersViewModel ViewModel for orders history
 * @param orderViewModel ViewModel for order processing
 * @author Sofia Bakalskaya
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    catalogViewModel: CatalogViewModel,
    detailsViewModel: DetailsViewModel,
    cartViewModel: CartViewModel,
    profileViewModel: ProfileViewModel,
    favouritesViewModel: FavouritesViewModel,
    allOrdersViewModel: AllOrdersViewModel,
    orderViewModel: OrderViewModel
) {
    val isAuthorized by authViewModel.isAuthorized.collectAsState()


    LaunchedEffect(isAuthorized) {
        if (isAuthorized == true)
            profileViewModel.fetchProfileUrl()
    }

    if (isAuthorized == false) {
        AuthNavHost(
            navController = navController,
            authViewModel = authViewModel
        )
    } else {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController, catalogViewModel = catalogViewModel)
            }
        ) {
            AppNavHost(
                navController = navController,
                catalogViewModel = catalogViewModel,
                detailsViewModel = detailsViewModel,
                cartViewModel = cartViewModel,
                profileViewModel = profileViewModel,
                favouritesViewModel = favouritesViewModel,
                allOrdersViewModel = allOrdersViewModel,
                orderViewModel = orderViewModel,
                logOut = { authViewModel.logOut() }
            )
        }
    }
}
package com.example.prac1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.prac1.app.MyApplication
import com.example.prac1.presentation.composable.MainScreen
import com.example.prac1.presentation.composable.SplashScreen
import com.example.prac1.presentation.viewmodel.AllOrdersViewModel
import com.example.prac1.presentation.viewmodel.AuthViewModel
import com.example.prac1.presentation.viewmodel.CartViewModel
import com.example.prac1.presentation.viewmodel.CatalogViewModel
import com.example.prac1.presentation.viewmodel.DetailsViewModel
import com.example.prac1.presentation.viewmodel.FavouritesViewModel
import com.example.prac1.presentation.viewmodel.OrderViewModel
import com.example.prac1.presentation.viewmodel.ProfileViewModel
import javax.inject.Inject

/**
 * Main activity class serving as entry point for the application
 *
 * Manages ViewModel initialization and navigation between splash/main screens
 *
 * @author Sofia Bakalskaya
 */
class MainActivity : ComponentActivity() {

    /**
     * Navigation controller for app navigation
     */
    private lateinit var navController: NavHostController

    /**
     * Dagger-injected ViewModel factory
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    /**
     * ViewModel for product details
     */
    private lateinit var detailsViewModel: DetailsViewModel
    /**
     * ViewModel for shopping cart
     */
    private lateinit var cartViewModel: CartViewModel
    /**
     * ViewModel for product catalog
     */
    private lateinit var catalogViewModel: CatalogViewModel
    /**
     * ViewModel for authentication
     */
    private lateinit var authViewModel: AuthViewModel
    /**
     * ViewModel for user profile
     */
    private lateinit var profileViewModel: ProfileViewModel
    /**
     * ViewModel for favorites
     */
    private lateinit var favouritesViewModel: FavouritesViewModel
    /**
     * ViewModel for order history
     */
    private lateinit var allOrdersViewModel: AllOrdersViewModel
    /**
     * ViewModel for order details
     */
    private lateinit var orderViewModel: OrderViewModel

    /**
     * Initializes activity and sets up UI components
     *
     * @param savedInstanceState Saved instance state bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        (application as MyApplication).appComponent.inject(this)
        detailsViewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class]
        cartViewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class]
        catalogViewModel = ViewModelProvider(this, viewModelFactory)[CatalogViewModel::class]
        authViewModel = ViewModelProvider(this, viewModelFactory) [AuthViewModel::class]
        profileViewModel = ViewModelProvider(this, viewModelFactory) [ProfileViewModel::class]
        favouritesViewModel = ViewModelProvider(this, viewModelFactory) [FavouritesViewModel::class]
        allOrdersViewModel = ViewModelProvider(this, viewModelFactory) [AllOrdersViewModel::class]
        orderViewModel = ViewModelProvider(this, viewModelFactory) [OrderViewModel::class]
        setContent {
            var showSplash by remember { mutableStateOf(false) }
            MaterialTheme {
                navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showSplash) {
                        SplashScreen {
                            showSplash = false
                        }
                    } else {
                        MainScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            catalogViewModel = catalogViewModel,
                            detailsViewModel = detailsViewModel,
                            cartViewModel = cartViewModel,
                            profileViewModel = profileViewModel,
                            favouritesViewModel = favouritesViewModel,
                            orderViewModel = orderViewModel,
                            allOrdersViewModel = allOrdersViewModel
                        )
                    }
                }
            }
        }
    }
}
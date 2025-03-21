package com.example.prac1.presentation.navigation

import com.example.prac1.R

/**
 * Data class representing an item in the bottom navigation bar.
 *
 * @param label Resource ID for the item's label.
 * @param icon Resource ID for the item's icon.
 * @param route Navigation route associated with the item.
 *
 * @author Sofia Bakalskaya
 */
data class BottomNavigationItem(
    val label: Int = 0,
    val icon: Int = 0,
    val route: String = ""
) {
    /**
     * Provides a list of bottom navigation items used in the navigation bar.
     *
     * @return A list of predefined `BottomNavigationItem` instances.
     */
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = R.string.home,
                icon = R.drawable.home_icon,
                route = Screens.Catalog.route
            ),
            BottomNavigationItem(
                label = R.string.my_cart,
                icon = R.drawable.cart_icon,
                route = Screens.Cart.route
            ),
            BottomNavigationItem(
                label = R.string.my_account,
                icon = R.drawable.user_icon,
                route = Screens.Profile.route
            ),
        )
    }
}

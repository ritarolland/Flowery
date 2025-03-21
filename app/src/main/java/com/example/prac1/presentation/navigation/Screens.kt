package com.example.prac1.presentation.navigation

/**
 * Sealed class representing the navigation destinations in the application.
 *
 * @property route The navigation route associated with the screen.
 *
 * @author Sofia Bakalskaya
 */
sealed class Screens(val route : String) {
    /** Authentication screen route. */
    object Auth: Screens("auth_route")
    /** Registration screen route. */
    object Register: Screens("register_route")
    /** Catalog screen route. */
    object Catalog : Screens("catalog_route")
    /** Cart screen route. */
    object Cart : Screens("cart_route")
    /** Profile screen route. */
    object Profile : Screens("profile_route")
    /** Favourites screen route. */
    object Favourites: Screens("favourites_root")
    /** Screen displaying all orders. */
    object AllOrders: Screens("all_orders_root")
    /**
     * Screen displaying details of a specific order.
     * Uses `orderId` as a path parameter for navigation.
     *
     * @param orderId The unique identifier of the order.
     */
    object Order: Screens("order_root/{orderId}") {
        /**
         * Generates a navigation route with the given order ID.
         *
         * @param orderId The order ID to be included in the route.
         * @return A formatted navigation route for the order screen.
         */
        fun orderId(orderId: String) = "order_root/$orderId"
    }
}
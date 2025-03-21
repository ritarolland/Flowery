package com.example.prac1.domain.repository

import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.model.CartItem
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

/**
 * Repository interface for managing the cart items and orders.
 * It provides functions to add, update, remove, and fetch cart items, as well as create and manage orders.
 * This repository handles the data layer interactions related to cart management and order processing.
 *
 * @author Eduard Pavlov
 */
interface CartRepository {
    /**
     * Retrieves the list of cart items.
     *
     * @return A [Flow] emitting the list of [CartItem] objects representing the items in the cart.
     */
    fun getCartItems(): Flow<List<CartItem>>
    /**
     * Adds a new item to the cart.
     *
     * @param cartItem The [CartItem] to be added to the cart.
     * @param onComplete A callback function that will be invoked once the item is added to the cart,
     *                   indicating whether the operation was successful.
     */
    fun addItemToCart(cartItem: CartItem, onComplete: (Boolean) -> Unit = {})
    /**
     * Updates the quantity of a specific cart item.
     *
     * @param itemId The ID of the cart item to be updated.
     * @param newQuantity The new quantity for the cart item.
     * @param onComplete A callback function that will be invoked once the quantity is updated,
     *                   indicating whether the operation was successful.
     */
    fun updateCartItemQuantity(itemId: String, newQuantity: Int, onComplete: (Boolean) -> Unit = {})
    /**
     * Updates the order ID associated with a cart item.
     *
     * @param itemId The ID of the cart item to be updated.
     * @param orderId The new order ID to be associated with the cart item.
     * @param onComplete A callback function that will be invoked once the order ID is updated,
     *                   indicating whether the operation was successful.
     */
    fun updateCartItemOrderId(itemId: String, orderId: String, onComplete: (Boolean) -> Unit = {})
    /**
     * Retrieves the cart item corresponding to a specific flower ID.
     *
     * @param flowerId The ID of the flower for which the cart item should be fetched.
     * @return The [CartItem] associated with the provided flower ID, or null if no item is found.
     */
    fun getCartItemByFlowerId(flowerId: String): CartItem?
    /**
     * Removes a cart item by its ID.
     *
     * @param itemId The ID of the cart item to be removed.
     * @param onComplete A callback function that will be invoked once the item is removed from the cart,
     *                   indicating whether the operation was successful.
     */
    fun removeItemFromCart(itemId: String, onComplete: (Boolean) -> Unit = {})
    /**
     * Creates a new order based on the current cart state.
     *
     * @param totalPrice The total price of the order.
     * @param timestamp The timestamp representing when the order was placed.
     * @return The [OrderDataModel] object representing the created order, or null if the order creation failed.
     */
    suspend fun createOrder(totalPrice: Double, timestamp: Timestamp): OrderDataModel?
    /**
     * Clears all items from the cart.
     */
    fun clearCart()
    /**
     * Adds an order to the repository.
     *
     * @param order The [OrderDataModel] representing the order to be added.
     */
    fun addOrder(order: OrderDataModel)
}
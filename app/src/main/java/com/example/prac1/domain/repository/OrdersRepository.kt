package com.example.prac1.domain.repository

import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing order-related data.
 * It provides functions to retrieve orders, order items, and individual flowers based on order IDs.
 * This repository interacts with the data layer related to order and flower management.
 *
 * @author Eduard Pavlov
 */
interface OrdersRepository {
    /**
     * Retrieves all orders placed by the user.
     *
     * @return A [Flow] emitting a list of [OrderDataModel] objects representing all the orders.
     */
    fun getOrders(): Flow<List<OrderDataModel>>

    /**
     * Retrieves a specific order based on its ID.
     *
     * @param orderId The ID of the order to retrieve.
     * @return A [Flow] emitting the [OrderDataModel] object corresponding to the given order ID.
     *         If no order is found, it emits `null`.
     */
    fun getOrderById(orderId: String): Flow<OrderDataModel?>

    /**
     * Retrieves the list of cart items associated with a specific order ID.
     *
     * @param orderId The ID of the order whose cart items need to be retrieved.
     * @return A [Flow] emitting a list of [CartItem] objects corresponding to the order ID.
     */
    fun getOrderItems(orderId: String): Flow<List<CartItem>>

    /**
     * Retrieves a flower by its ID.
     *
     * @param flowerId The ID of the flower to retrieve.
     * @return A [Flower] object representing the flower with the specified ID, or `null` if not found.
     */
    suspend fun getFlowerById(flowerId: String): Flower?
}
package com.example.prac1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state of an order and its associated items (flowers).
 * It communicates with the OrdersRepository to fetch order data and CartRepository for cart item data.
 *
 * @param ordersRepository The repository responsible for managing order data.
 * @param cartRepository The repository responsible for managing cart item data.
 *
 * @author Sofia Bakalskaya
 */
class OrderViewModel@Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val cartRepository: CartRepository
): ViewModel() {
    /** MutableStateFlow holding the current order data. */
    private val _order = MutableStateFlow<OrderDataModel?>(null)
    /** Publicly exposed StateFlow for observing the order data. */
    val order = _order.asStateFlow()

    /** MutableStateFlow holding a map of cart items and their associated flower details. */
    private val _flowersMap = MutableStateFlow<Map<CartItem, Flower>>(emptyMap())
    /** Publicly exposed StateFlow for observing the flowers map. */
    val flowersMap = _flowersMap.asStateFlow()

    /**
     * Loads flower details for a given cart item and updates the flowers map.
     *
     * @param cartItem The cart item for which the flower details are to be loaded.
     */
    private fun loadFlower(cartItem: CartItem) {
        viewModelScope.launch {
            val flower = ordersRepository.getFlowerById(cartItem.flowerId)
            if (flower != null) {
                _flowersMap.value += (cartItem to flower)
            } else Log.d("ORDER VM", "flower null")
        }
    }

    /**
     * Loads order data and associated cart items for a given order ID.
     *
     * @param orderId The ID of the order to load.
     */
    fun loadData(orderId: String) {
        viewModelScope.launch {
            ordersRepository.getOrderById(orderId).collect { order ->
                _order.value = order
            }
            ordersRepository.getOrderItems(orderId).collect { items ->
                items.forEach { cartItem ->
                    loadFlower(cartItem)
                }
            }
        }
    }

    /**
     * Clears the order and flowers map data.
     */
    fun clearData() {
        _order.value = null
        _flowersMap.value = emptyMap()
    }
}
package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the state and logic related to all user orders.
 *
 * @property ordersRepository Repository responsible for fetching order data.
 *
 * @author Sofia Bakalskaya
 */
class AllOrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository
): ViewModel() {
    /** StateFlow holding the list of user orders. */
    private val _orders = MutableStateFlow<List<OrderDataModel>>(emptyList())
    /** Publicly exposed StateFlow to observe the list of orders. */
    val orders = _orders.asStateFlow()

    init {
        viewModelScope.launch {
            loadOrders()
        }
    }

    /**
     * Loads all orders from the repository and updates the state.
     * This function collects the flow of orders and updates the `_orders` state.
     */
    suspend fun loadOrders() {
        ordersRepository.getOrders().collect { items ->
            _orders.value = items
        }
    }
}
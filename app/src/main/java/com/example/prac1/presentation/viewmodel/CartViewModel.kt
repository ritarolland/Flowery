package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.FlowersRepository
import com.example.prac1.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Timestamp
import javax.inject.Inject

/**
 * ViewModel responsible for managing cart functionality, including item selection,
 * quantity updates, and order placement.
 *
 * @param cartRepository Repository handling cart operations.
 * @param catalogRepository Repository providing catalog items.
 * @param ordersRepository Repository handling order management.
 *
 * @author Sofia Bakalskaya
 */
class CartViewModel@Inject constructor(
    private val cartRepository: CartRepository,
    private val catalogRepository: FlowersRepository,
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    /** StateFlow holding the list of cart items. */
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    /** Publicly exposed StateFlow to observe cart items. */
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    /** StateFlow holding the total cost of selected cart items. */
    private val _totalCost = MutableStateFlow(0.0)
    /** Publicly exposed StateFlow for observing total cost. */
    val totalCost = _totalCost.asStateFlow()

    /** StateFlow holding the list of selected cart item IDs. */
    private val _selectedItems = MutableStateFlow<List<String>>(emptyList())
    /** Publicly exposed StateFlow for observing selected items. */
    val selectedItems = _selectedItems.asStateFlow()

    /** StateFlow indicating whether selection mode is active. */
    private val _isSelectionMode = MutableStateFlow(false)
    /** Publicly exposed StateFlow for observing selection mode state. */
    val isSelectionMode: StateFlow<Boolean> = _isSelectionMode.asStateFlow()

    /** StateFlow holding the list of catalog items. */
    private val _catalogItems = MutableStateFlow<List<Flower>>(emptyList())

    init {
        loadCatalogItems()
        loadCartItems()
    }

    /**
     * Updates the total cost based on selected cart items.
     */
    private fun updateTotalCost() {
        val cartItemsSelected = _cartItems.value.filter { it.id in _selectedItems.value }
        _totalCost.value = cartItemsSelected.sumOf { cartItem ->
            val catalogItem = _catalogItems.value.firstOrNull { it.id == cartItem.flowerId }
            catalogItem?.let { item ->
                cartItem.quantity * item.price
            } ?: 0.0
        }
    }

    /**
     * Deletes all selected cart items.
     */
    fun deleteSelectedCartItems() {
        viewModelScope.launch {
            for (cartItemId in _selectedItems.value) {
                cartRepository.removeItemFromCart(cartItemId)
            }
            toggleSelectionMode()
        }
    }

    /**
     * Loads cart items from the repository.
     */
    private fun loadCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems().collect { items ->
                _cartItems.value = items
            }
        }
    }

    /**
     * Loads catalog items from the repository.
     */
    private fun loadCatalogItems() {
        viewModelScope.launch {
            catalogRepository.getCatalogItems().collect { items ->
                _catalogItems.value = items
            }
        }
    }

    /**
     * Retrieves a catalog item by its ID.
     *
     * @param id The item ID.
     * @return The corresponding Flower object if found, otherwise null.
     */
    fun getItemById(id: String): Flower? {
        return _catalogItems.value.find { it.id == id }
    }

    /**
     * Updates the quantity of a cart item or removes it if quantity is zero.
     *
     * @param itemId The cart item ID.
     * @param newQuantity The new quantity to set.
     */
    fun updateCartItemQuantity(itemId: String, newQuantity: Int) {
        if (newQuantity == 0) cartRepository.removeItemFromCart(itemId)
        else cartRepository.updateCartItemQuantity(itemId, newQuantity)
    }

    /**
     * Places an order with the selected items.
     *
     * @param timestamp The timestamp for the order placement.
     */
    fun placeOrder(timestamp: Timestamp) {
        viewModelScope.launch {
            val order = cartRepository.createOrder(totalPrice = _totalCost.value, timestamp = timestamp)
            if (order != null) {
                cartRepository.addOrder(order)
                for (cartItemId in _selectedItems.value) {
                    cartRepository.updateCartItemOrderId(cartItemId, order.id)
                }
                toggleSelectionMode()
            }
        }
    }

    /**
     * Toggles selection mode on or off.
     */
    fun toggleSelectionMode() {
        _isSelectionMode.value = !_isSelectionMode.value
        if (!_isSelectionMode.value) {
            _selectedItems.value = emptyList()
            updateTotalCost()
        }
    }

    /**
     * Toggles the selection state of a cart item.
     *
     * @param itemId The ID of the item to toggle selection.
     */
    fun toggleSelection(itemId: String) {
        _selectedItems.value = if (_selectedItems.value.contains(itemId)) {
            _selectedItems.value.filter { it != itemId }
        } else {
            _selectedItems.value + itemId
        }
        updateTotalCost()
    }
}
package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.FlowersRepository
import com.example.prac1.domain.repository.UserUidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel responsible for managing the details of a flower item, including loading the item, adding it to the cart,
 * and updating the cart items.
 *
 * @param catalogRepository Responsible for fetching catalog items.
 * @param cartRepository Responsible for managing cart items.
 * @param userUidRepository Responsible for retrieving the user's UID.
 *
 * @author Sofia Bakalskaya
 */
class DetailsViewModel @Inject constructor(
    private val catalogRepository: FlowersRepository,
    private val cartRepository: CartRepository,
    private val userUidRepository: UserUidRepository
) : ViewModel() {

    /** MutableStateFlow holding the selected flower item. */
    private val _selectedItem = MutableStateFlow<Flower?>(null)
    /** Publicly exposed StateFlow for observing the selected flower item. */
    val selectedItem = _selectedItem.asStateFlow()

    /** MutableStateFlow holding the cart item associated with the selected flower. */
    private val _cartItem = MutableStateFlow<CartItem?>(null)
    /** Publicly exposed StateFlow for observing the flower count in the cart. */
    val flowerCountInCart = _cartItem.asStateFlow()

    /**
     * Loads the flower item by its ID and updates the selected item.
     *
     * @param itemId The ID of the flower item to be loaded.
     */
    fun loadItemById(itemId: String) {
        viewModelScope.launch {
            val items = catalogRepository.getCatalogItems()
            items.collect { itemList ->
                _selectedItem.value = itemList.find { it.id == itemId } ?: Flower()
                updateCartItem()
            }
        }
    }

    /**
     * Updates the cart item associated with the selected flower.
     */
    private suspend fun updateCartItem() {
        _cartItem.emit(cartRepository.getCartItemByFlowerId(_selectedItem.value?.id ?: ""))
    }


    /**
     * Clears the currently selected flower item.
     */
    fun clearSelectedItem() {
        _selectedItem.value = null
    }

    /**
     * Adds the selected flower item to the cart.
     * If the flower is already in the cart, it will update the quantity.
     */
    fun addItemToCart() {
        viewModelScope.launch {
            val uid = userUidRepository.getUserUid() ?: return@launch
            if (_selectedItem.value != null) {
                val cartItem = CartItem(
                    id = UUID.randomUUID().toString(),
                    flowerId = _selectedItem.value!!.id,
                    userId = uid,
                    quantity = 1
                )
                cartRepository.addItemToCart(cartItem) { success ->
                    if (success) {
                        viewModelScope.launch {
                            updateCartItem()
                        }
                    } else {
                        viewModelScope.launch {
                            updateCartItem()
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates the quantity of the cart item for the given item ID.
     * If the new quantity is zero, the item will be removed from the cart.
     *
     * @param itemId The ID of the cart item.
     * @param newQuantity The new quantity to set for the cart item.
     */
    fun updateCartItemQuantity(itemId: String, newQuantity: Int) {
        if (newQuantity == 0) {
            deleteItemFromCart(itemId)
        } else {
            cartRepository.updateCartItemQuantity(
                itemId,
                newQuantity,
                onComplete = { isSuccessful ->
                    if (isSuccessful) {
                        viewModelScope.launch {
                            updateCartItem()
                        }
                    }
                })
        }
    }

    /**
     * Removes the cart item with the specified ID from the cart.
     *
     * @param itemId The ID of the cart item to remove.
     */
    private fun deleteItemFromCart(itemId: String) {
        cartRepository.removeItemFromCart(itemId) { isSuccessful ->
            if (isSuccessful) {
                viewModelScope.launch {
                    updateCartItem()
                }
            }
        }
    }
}
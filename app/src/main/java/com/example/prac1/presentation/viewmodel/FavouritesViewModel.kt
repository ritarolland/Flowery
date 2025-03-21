package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.FavouritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the list of favourite flowers and toggling the favorite status of flowers.
 * It communicates with the repository to fetch and update the favourite flowers data.
 *
 * @param favouritesRepository The repository responsible for managing the favourites data.
 *
 * @author Sofia Bakalskaya
 */
class FavouritesViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository
): ViewModel() {
    /** MutableStateFlow holding the list of favourite flowers. */
    private val _favouriteFlowers = MutableStateFlow<List<Flower>>(emptyList())
    /** Publicly exposed StateFlow for observing the list of favourite flowers. */
    val favouriteFlowers = _favouriteFlowers.asStateFlow()

    init {
        loadFavouriteFlowers()
    }

    /**
     * Loads the favourite flowers from the repository and updates the StateFlow.
     */
    private fun loadFavouriteFlowers() {
        viewModelScope.launch {
            favouritesRepository.getFavouriteFlowers().collect{ items ->
                _favouriteFlowers.value = items
            }
        }
    }

    /**
     * Toggles the favourite status of a flower by its ID.
     * It calls the repository's method to toggle the favourite status.
     *
     * @param flowerId The ID of the flower whose favourite status needs to be toggled.
     */
    fun toggleIsFavourite(flowerId: String) {
        favouritesRepository.toggleIsFavourite(flowerId)
    }
}
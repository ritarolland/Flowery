package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.FavouritesRepository
import com.example.prac1.domain.repository.FlowersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing catalog items, search functionality, and favorite items.
 *
 * @param repository Handles fetching catalog items.
 * @param favouritesRepository Manages favorite items.
 *
 * @author Sofia Bakalskaya
 */
class CatalogViewModel@Inject constructor(
    private val repository: FlowersRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {
    /** MutableStateFlow holding the current search query. */
    private val _searchQuery = MutableStateFlow("")
    /** Publicly exposed StateFlow for observing search query changes. */
    val searchQuery = _searchQuery.asStateFlow()

    /** MutableStateFlow holding all catalog items. */
    private val _allCatalogItems = MutableStateFlow<List<Flower>>(emptyList())

    /** MutableStateFlow holding the list of favorite flower IDs. */
    private val _favourites = MutableStateFlow<List<String>>(emptyList())
    /** Publicly exposed StateFlow for observing favorite items. */
    val favourites = _favourites.asStateFlow()

    /**
     * StateFlow that filters catalog items based on the search query.
     * If the query is empty, all items are displayed.
     */
    val catalogItems: StateFlow<List<Flower>> = combine(
        _searchQuery,
        _allCatalogItems
    ) { query, items ->
        if (query.isBlank()) items
        else items.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadCatalogItems()
        loadFavourites()
    }

    /**
     * Loads the list of favorite items from the repository.
     */
    private fun loadFavourites() {
        viewModelScope.launch {
            favouritesRepository.getFavouriteFlowers().collect { items ->
                _favourites.value = items.map { it.id }
            }
        }
    }

    /**
     * Loads the catalog items from the repository.
     */
    private fun loadCatalogItems() {
        viewModelScope.launch {
            repository.getCatalogItems().collect { items ->
                _allCatalogItems.value = items
            }
        }
    }

    /**
     * Toggles the favorite status of a flower.
     *
     * @param flowerId The ID of the flower to toggle.
     */
    fun toggleIsFavourite(flowerId: String) {
        favouritesRepository.toggleIsFavourite(flowerId)
    }

    /**
     * Updates the current search query.
     *
     * @param query The new search query.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Checks if a flower is in the favorites list.
     *
     * @param id The ID of the flower.
     * @return True if the flower is a favorite, false otherwise.
     */
    fun isFavorite(id: String): Boolean {
        return id in favourites.value
    }
}
package com.example.prac1.domain.repository

import com.example.prac1.domain.model.Flower
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing favourite flowers.
 * It provides functions to retrieve and toggle the favourite status of flowers.
 * This repository handles the data layer interactions related to favourite flowers.
 *
 * @author Eduard Pavlov
 */
interface FavouritesRepository {
    /**
     * Retrieves the list of favourite flowers.
     *
     * @return A [Flow] emitting the list of [Flower] objects representing the user's favourite flowers.
     */
    fun getFavouriteFlowers(): Flow<List<Flower>>
    /**
     * Toggles the favourite status of a flower.
     * If the flower is already marked as a favourite, it will be removed; if not, it will be added.
     *
     * @param flowerId The ID of the flower whose favourite status should be toggled.
     */
    fun toggleIsFavourite(flowerId: String)
}
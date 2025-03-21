package com.example.prac1.domain.repository

import com.example.prac1.domain.model.Flower
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing flower catalog data.
 * It provides functions to retrieve the flower catalog and to cache flowers.
 * This repository handles the data layer interactions related to flowers.
 *
 * @author Eduard Pavlov
 */
interface FlowersRepository {
    /**
     * Retrieves the list of flower catalog items.
     *
     * @return A [Flow] emitting a list of [Flower] objects representing the flowers in the catalog.
     */
    suspend fun getCatalogItems(): Flow<List<Flower>>
    /**
     * Saves a list of flowers to the cache.
     * This is used to store the flower data locally for offline access or faster retrieval.
     *
     * @param flowers A list of [Flower] objects to be saved to the cache.
     */
    suspend fun saveFlowersToCache(flowers: List<Flower>)
}
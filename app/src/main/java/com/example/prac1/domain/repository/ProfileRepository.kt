package com.example.prac1.domain.repository

import com.example.prac1.data.api.model.UserInfoDataModel
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user profile-related data.
 * It provides functions to retrieve user information and initiate a fetch operation for user data.
 * This repository interacts with the data layer related to user profiles.
 *
 * @author Eduard Pavlov
 */
interface ProfileRepository {
    /**
     * Retrieves the user information.
     *
     * @return A [Flow] emitting the [UserInfoDataModel] object representing the user's profile.
     *         If the user information is not available, it will emit `null`.
     */
    suspend fun getUserInfo(): Flow<UserInfoDataModel?>

    /**
     * Initiates the fetching of user information.
     * This is a side-effect function, typically used to refresh or retrieve updated user data.
     */
    fun fetchInfo()
}
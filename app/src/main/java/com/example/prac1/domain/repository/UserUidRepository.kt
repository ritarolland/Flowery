package com.example.prac1.domain.repository

/**
 * Repository interface responsible for managing the user's unique identifier (UID).
 * It provides functions to retrieve, store, and clear the UID.
 *
 * @author Eduard Pavlov
 */
interface UserUidRepository {
    /**
     * Retrieves the stored user UID.
     *
     * @return A [String] representing the user's UID, or `null` if the UID is not stored.
     */
    suspend fun getUserUid(): String?
    /**
     * Sets the user UID.
     *
     * @param uid The unique identifier of the user to be stored.
     */
    fun setUserUid(uid: String)
    /**
     * Clears the stored user UID.
     */
    fun clearUid()
}
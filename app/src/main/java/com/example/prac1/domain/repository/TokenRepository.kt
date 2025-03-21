package com.example.prac1.domain.repository

import retrofit2.Response

/**
 * Repository interface responsible for managing authentication tokens, including refresh tokens.
 * It provides functions to handle token operations such as refreshing, storing, retrieving, and checking token validity.
 *
 * @author Eduard Pavlov
 */
interface TokenRepository {
    /**
     * Refreshes the authentication token by making an API call.
     *
     * @return A [Boolean] indicating whether the token was successfully refreshed.
     */
    suspend fun refreshToken(): Boolean
    /**
     * Executes an API call and handles success or failure through callback functions.
     * This function allows for a flexible handling of API responses and errors.
     *
     * @param apiCall The suspend function that represents the API call to be executed.
     * @param onSuccess A callback that is invoked when the API call succeeds. Defaults to an empty function.
     * @param onError A callback that is invoked when the API call fails. Defaults to an empty function.
     */
    suspend fun <T> executeApiCall(
        apiCall: suspend () -> Response<T>,
        onSuccess: (Response<T>) -> Unit = {},
        onError: (message: String) -> Unit = {}
    )
    /**
     * Executes an API call and returns the response.
     * This function is used when you want to handle the API response directly, without custom success or error handlers.
     *
     * @param apiCall The suspend function representing the API call to be executed.
     * @return The [Response] from the API call, or `null` if the call fails or is not handled.
     */
    suspend fun <T> executeApiCall(
        apiCall: suspend () -> Response<T>
    ): Response<T>?
    /**
     * Creates an authorization header containing the current authentication token.
     *
     * @return A string representing the Authorization header, e.g., "Bearer <token>".
     */
    fun createAuthHeader(): String
    /**
     * Retrieves the current authentication token.
     *
     * @return A [String] containing the current token, or `null` if no token is available.
     */
    fun getToken(): String?
    /**
     * Sets the authentication token along with its expiration time.
     *
     * @param token The authentication token to store.
     * @param expiresIn The expiration time of the token in milliseconds.
     */
    fun setToken(token: String, expiresIn: Long)
    /**
     * Clears the stored authentication token.
     */
    fun clearToken()
    /**
     * Retrieves the current refresh token.
     *
     * @return A [String] containing the current refresh token, or `null` if no refresh token is available.
     */
    fun getRefreshToken(): String?
    /**
     * Sets the refresh token.
     *
     * @param refreshToken The refresh token to store.
     */
    fun setRefreshToken(refreshToken: String)
    /**
     * Clears the stored refresh token.
     */
    fun clearRefreshToken()
    /**
     * Checks if the current authentication token has expired.
     *
     * @return A [Boolean] indicating whether the token has expired or not.
     */
    fun isTokenExpired(): Boolean
}
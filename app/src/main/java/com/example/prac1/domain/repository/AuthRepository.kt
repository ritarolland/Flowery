package com.example.prac1.domain.repository

import com.example.prac1.data.repository.AuthState
import java.io.File

/**
 * Repository interface for managing authentication-related tasks such as signing in, signing up,
 * checking user authorization, logging out, uploading images to Supabase, and updating user information.
 * This interface provides functions that interact with the data layer to handle authentication
 * and user data.
 *
 * @author Eduard Pavlov
 */
interface AuthRepository {
    /**
     * Signs in the user by providing their email and password.
     *
     * @param email The email of the user attempting to sign in.
     * @param password The password of the user attempting to sign in.
     * @return An [AuthState] object representing the state of the sign-in process,
     *         which can indicate success, failure, or loading.
     */
    suspend fun signIn(email: String, password: String): AuthState

    /**
     * Signs up a new user by providing their email and password.
     *
     * @param email The email of the user attempting to sign up.
     * @param password The password of the user attempting to sign up.
     * @return An [AuthState] object representing the state of the sign-up process,
     *         which can indicate success, failure, or loading.
     */
    suspend fun signUp(email: String, password: String): AuthState

    /**
     * Checks whether the user is authorized and logged in.
     *
     * @return A Boolean indicating whether the user is currently authorized.
     */
    suspend fun isUserAuthorized(): Boolean

    /**
     * Logs out the current user, clearing their authentication session.
     */
    fun logOut()

    /**
     * Uploads an image file to Supabase storage.
     *
     * @param imageFile The image file to be uploaded.
     * @param fileName The name of the file to be stored in Supabase.
     * @return A String representing the URL of the uploaded image, or null if the upload failed.
     */
    suspend fun uploadImageToSupabase(imageFile: File, fileName: String): String?

    /**
     * Uploads updated user information (e.g., name, email, and image URL) to Supabase.
     *
     * @param name The name of the user.
     * @param imageUrl The URL of the user's image, or null if no image is provided.
     * @param email The email of the user.
     * @return An [AuthState] object representing the state of the user info upload process,
     *         which can indicate success, failure, or loading.
     */
    suspend fun uploadUserInfo(name: String, imageUrl: String?, email: String): AuthState
}
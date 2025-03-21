package com.example.prac1.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.repository.AuthState
import com.example.prac1.domain.repository.AuthRepository
import com.example.prac1.domain.repository.UserUidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel responsible for handling authentication logic, including sign-in, sign-up,
 * image uploads, and authorization checks.
 *
 * @param authRepository Repository handling authentication operations.
 * @param uidRepository Repository managing user UID retrieval.
 *
 * @author Sofia Bakalskaya
 */
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository,
    private val uidRepository: UserUidRepository) : ViewModel() {

    /** StateFlow representing the authentication state (e.g., loading, success, error). */
    private val _signInState = MutableStateFlow<AuthState>(AuthState.Default)
    /** Publicly exposed authentication state to observe UI changes. */
    val signInState = _signInState.asStateFlow()
    /** StateFlow representing whether the user is authorized. */
    private val _isAuthorized = MutableStateFlow<Boolean?>(null)
    /** Publicly exposed authorization state. */
    val isAuthorized = _isAuthorized.asStateFlow()

    init {
        viewModelScope.launch {
            checkAuthorization()
        }
    }

    /**
     * Retrieves the real file path from a given URI.
     *
     * @param context The application context.
     * @param contentUri The URI of the image file.
     * @return The real file path as a string.
     */
    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        val cursor = context.contentResolver.query(contentUri, null, null, null, null)
        cursor?.let {
            it.moveToFirst()
            val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = it.getString(idx)
            it.close()
            return path
        }
        return ""
    }

    /**
     * Uploads an image to Supabase storage.
     *
     * @param imageUri The URI of the image to upload.
     * @param context The application context.
     * @return The URL of the uploaded image if successful, otherwise null.
     */
    private suspend fun uploadImage(imageUri: Uri?, context: Context): String? {
        if (imageUri != null) {
            val imageFile = File(getRealPathFromURI(context, imageUri))
            val fileName = "profile_picture_${System.currentTimeMillis()}.jpg"
            if (!imageFile.exists()) {
                return null
            }
            return authRepository.uploadImageToSupabase(imageFile, fileName)
        }
        return null
    }

    /**
     * Handles the sign-in process.
     *
     * @param email The user's email.
     * @param password The user's password.
     */
    fun signIn(email: String, password: String) {
        _signInState.value = AuthState.Loading
        viewModelScope.launch {
            val state = authRepository.signIn(email, password)
            _signInState.value = state
            if (state is AuthState.Success) {
                checkAuthorization()
            }
        }
    }

    /**
     * Handles the sign-up process, including user registration and optional profile image upload.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @param imageUri Optional URI of the user's profile picture.
     * @param context The application context.
     * @param name The user's display name.
     */
    fun signUp(email: String, password: String, imageUri: Uri?, context: Context, name: String) {
        _signInState.value = AuthState.Loading
        viewModelScope.launch {
            val state1 = authRepository.signUp(email, password)
            val url = uploadImage(imageUri, context)
            val state2 = authRepository.uploadUserInfo(name, url, email)
            val state = when {
                state1 is AuthState.Success && state2 is AuthState.Success -> AuthState.Success
                state1 is AuthState.Error -> state1
                else -> state2
            }
            _signInState.value = state
            if (state is AuthState.Success) {
                checkAuthorization()
            }
        }
    }

    /**
     * Checks whether the user is currently authorized.
     */
    private suspend fun checkAuthorization() {
        val authorized = authRepository.isUserAuthorized()
        _isAuthorized.value = authorized
    }

    /**
     * Logs the user out and resets authentication state.
     */
    fun logOut() {
        viewModelScope.launch {
            authRepository.logOut()
            _signInState.value = AuthState.Default
            checkAuthorization()
        }
    }

}
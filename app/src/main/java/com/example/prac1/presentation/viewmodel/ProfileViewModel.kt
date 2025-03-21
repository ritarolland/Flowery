package com.example.prac1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.api.model.UserInfoDataModel
import com.example.prac1.domain.repository.ProfileRepository
import com.example.prac1.domain.repository.UserUidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing and providing the user's profile information.
 * It interacts with the ProfileRepository to fetch and update user data and UserUidRepository for handling UID.
 *
 * @param profileRepository The repository responsible for managing profile data.
 * @param userUidRepository The repository responsible for managing the user's UID.
 *
 * @author Sofia Bakalskaya
 */
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userUidRepository: UserUidRepository
) : ViewModel() {

    /** MutableStateFlow holding the user's profile information. */
    private val _userInfo = MutableStateFlow<UserInfoDataModel?>(null)
    /** Publicly exposed StateFlow for observing the user profile data. */
    val userInfo = _userInfo.asStateFlow()

    init {
        loadUserInfo()
    }

    /**
     * Loads the user's profile information from the ProfileRepository and updates the _userInfo state.
     */
    private fun loadUserInfo() {
        viewModelScope.launch {
            profileRepository.getUserInfo().collect { info ->

                Log.d("REPOOOOO", "UserInfo in viewModel is $info")
                _userInfo.value = info
            }
        }
    }

    /**
     * Fetches the user's profile URL from the repository. (Assuming it triggers a backend call or another repository method).
     */
    fun fetchProfileUrl() {
        profileRepository.fetchInfo()
    }

    /**
     * Clears the user's UID by calling the corresponding method in UserUidRepository.
     * This is typically used for logging out the user.
     */
    fun logOut() {
        userUidRepository.clearUid()
    }

}
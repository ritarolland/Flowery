package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.prac1.data.repository.AuthResult
import com.example.prac1.domain.repository.UserUidRepository

class AuthViewModel @Inject constructor(private val authRepository: AuthRepository,
    private val uidRepository: UserUidRepository) : ViewModel() {
    private val _signInState = MutableStateFlow<AuthResult<Boolean>?>(null)
    val signInState = _signInState.asStateFlow()

    private val _isAuthorized = MutableStateFlow<Boolean?>(null)
    val isAuthorized = _isAuthorized.asStateFlow()

    init {
        viewModelScope.launch {
            checkAuthorization()
        }
    }

    fun signIn(email: String, password: String) {
        _signInState.value = AuthResult.Loading
        viewModelScope.launch {
            val success = authRepository.signIn(email, password)
            _signInState.value = AuthResult.Success(success)
            checkAuthorization()
        }
    }

    private suspend fun checkAuthorization() {
        val authorized = authRepository.isUserAuthorized()
        _isAuthorized.value = authorized
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.logOut()
            checkAuthorization()
        }
    }

}
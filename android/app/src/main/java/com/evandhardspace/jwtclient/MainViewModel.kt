package com.evandhardspace.jwtclient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.jwtclient.data.AuthRepository
import com.evandhardspace.jwtclient.data.AuthResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repo: AuthRepository,
) : ViewModel() {

    init {
        authenticate()
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    fun signUp(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.signUp(username, password)
            resultChannel.send(result)
            _isLoading.value = false
        }
    }

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.signIn(username, password)
            resultChannel.send(result)
            _isLoading.value = false
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.authenticate()
            resultChannel.send(result)
            _isLoading.value = false
        }
    }


}
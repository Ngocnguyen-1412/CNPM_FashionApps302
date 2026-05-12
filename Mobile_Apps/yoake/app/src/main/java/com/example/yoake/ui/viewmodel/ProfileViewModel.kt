package com.example.yoake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yoake.data.repository.YoakeRepository
import com.example.yoake.ui.screens.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: YoakeRepository = YoakeRepository(),
    private val userId: Int = 1
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState

    init { loadProfile() }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)
            repository.getUser(userId)
                .onSuccess { user ->
                    _uiState.value = ProfileUiState(isLoading = false, user = user)
                }
                .onFailure {
                    _uiState.value = ProfileUiState(isLoading = false)
                }
        }
    }
}

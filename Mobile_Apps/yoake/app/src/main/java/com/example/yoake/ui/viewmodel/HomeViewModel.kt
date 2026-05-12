package com.example.yoake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yoake.data.repository.YoakeRepository
import com.example.yoake.ui.screens.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
//  HomeViewModel
//
//  To use with Hilt, replace the constructor default with:
//    @HiltViewModel
//    class HomeViewModel @Inject constructor(
//        private val repository: YoakeRepository
//    ) : ViewModel()
// ─────────────────────────────────────────────────────────────────────────────

class HomeViewModel(
    private val repository: YoakeRepository = YoakeRepository(),
    private val userId: Int = 1              // replace with auth session user ID
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init { loadHome() }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            repository.getHomeData(userId)
                .onSuccess  { _uiState.value = it }
                .onFailure  { _uiState.value = HomeUiState(isLoading = false) }
        }
    }
}

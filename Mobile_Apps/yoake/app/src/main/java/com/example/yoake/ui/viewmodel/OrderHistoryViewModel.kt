package com.example.yoake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yoake.data.repository.YoakeRepository
import com.example.yoake.data.repository.toDomain
import com.example.yoake.ui.screens.OrderHistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val repository: YoakeRepository = YoakeRepository(),
    private val userId: Int = 1
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderHistoryUiState(isLoading = true))
    val uiState: StateFlow<OrderHistoryUiState> = _uiState

    private var currentPage = 1

    init { loadOrders() }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = OrderHistoryUiState(isLoading = true)
            currentPage = 1
            repository.getOrders(userId, page = 1)
                .onSuccess { page ->
                    _uiState.value = OrderHistoryUiState(
                        isLoading   = false,
                        orders      = page.orders.map { it.toDomain() },
                        totalCount  = page.total_count,
                        canLoadMore = page.orders.size < page.total_count
                    )
                }
                .onFailure {
                    _uiState.value = OrderHistoryUiState(isLoading = false)
                }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (!state.canLoadMore) return
        viewModelScope.launch {
            currentPage++
            repository.getOrders(userId, page = currentPage)
                .onSuccess { page ->
                    val combined = state.orders + page.orders.map { it.toDomain() }
                    _uiState.value = state.copy(
                        orders      = combined,
                        canLoadMore = combined.size < page.total_count
                    )
                }
        }
    }
}

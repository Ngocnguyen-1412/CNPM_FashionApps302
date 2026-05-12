package com.example.yoake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yoake.data.repository.YoakeRepository
import com.example.yoake.ui.screens.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: YoakeRepository = YoakeRepository(),
    private val userId: Int = 1             // replace with auth session user ID
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState(isLoading = true))
    val uiState: StateFlow<ProductDetailUiState> = _uiState

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState(isLoading = true)
            repository.getProduct(productId)
                .onSuccess { product ->
                    _uiState.value = ProductDetailUiState(isLoading = false, product = product)
                }
                .onFailure {
                    _uiState.value = ProductDetailUiState(isLoading = false)
                }
        }
    }

    fun addToCart(productId: String, color: String, size: String) {
        viewModelScope.launch {
            repository.addToCart(userId, productId, color, size)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(addedToCart = true)
                }
                // silently ignore errors — add toast/snackbar handling here
        }
    }

    fun resetAddedToCart() {
        _uiState.value = _uiState.value.copy(addedToCart = false)
    }
}

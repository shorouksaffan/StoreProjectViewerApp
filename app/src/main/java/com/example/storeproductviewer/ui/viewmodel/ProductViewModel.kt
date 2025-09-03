package com.example.storeproductviewer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeproductviewer.ui.viewmodel.Result
import com.example.storeproductviewer.data.remote.RetrofitInstance
import com.example.storeproductviewer.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<Result<List<Product>>>(Result.Loading)
    val products: StateFlow<Result<List<Product>>> = _products.asStateFlow()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            _products.value = Result.Loading
            try {
                val response = RetrofitInstance.apiService.getProducts()
                _products.value = Result.Success(response)
            } catch (e: Exception) {
                _products.value = Result.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
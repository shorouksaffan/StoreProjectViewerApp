package com.example.storeproductviewer.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.storeproductviewer.data.model.Product
import com.example.storeproductviewer.data.remote.RetrofitInstance


@Composable
fun DetailScreen(productId: Int) {
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(productId) {
        try {
            product = RetrofitInstance.apiService.getProduct(productId)
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.message ?: "Unknown error occurred"
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Error: $errorMessage",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else if (product != null) {
        val currentProduct = product!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (currentProduct.images.isNotEmpty()) {
                AsyncImage(
                    model = currentProduct.images.first(),
                    contentDescription = currentProduct.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = currentProduct.title, style = MaterialTheme.typography.headlineMedium)
            Text(text = "$${currentProduct.price}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = currentProduct.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

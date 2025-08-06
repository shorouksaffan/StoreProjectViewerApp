package com.example.storeproductviewer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun HomeScreen(viewModel: ProductViewModel, navController: NavController) {
    val productsState by viewModel.products.collectAsState()

    when (val state = productsState) {
        is Result.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is Result.Success -> {
            ProductList(products = state.data, navController = navController)
        }
        is Result.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Error: ${state.message}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ProductList(products: List<Product>, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product, onClick = {
                navController.navigate("detail/${product.id}")
            })
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (product.images.isNotEmpty()) {
                AsyncImage(
                    model = product.images.first(),
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "$${product.price}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

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

package com.example.storeproductviewer.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.storeproductviewer.ui.viewmodel.ProductViewModel
import com.example.storeproductviewer.ui.viewmodel.Result

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


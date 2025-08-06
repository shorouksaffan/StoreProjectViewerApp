package com.example.storeproductviewer

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val images: List<String>,
    val category: Category
)

@Serializable
data class Category(
    val id: Int,
    val name: String,
    val image: String
)
package com.example.prac1.data.api.model

data class FlowerDataModel(
    val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val image_url: String
)
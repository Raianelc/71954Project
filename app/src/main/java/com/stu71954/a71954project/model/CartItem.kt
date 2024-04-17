package com.stu71954.a71954project.model

data class CartItem(
    val id: Product,
    val userId: Int,
    val items: List<Map<String, Any>>,
    val total: Double,
    val product: Product,
    val quantity: Int
)
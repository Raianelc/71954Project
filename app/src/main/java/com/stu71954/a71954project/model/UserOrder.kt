package com.stu71954.a71954project.model

data class UserOrder (

        val id: String,
        val userId: String,
        val items: List<Map<String, Any>>,
        val total: Double,
)
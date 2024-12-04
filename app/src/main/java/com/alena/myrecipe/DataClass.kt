package com.alena.myrecipe

data class DataClass(
    val id: String = "",
    val name: String = "",
    val ingredients: List<String> = listOf(),  // Ubah menjadi List<String>
    val steps: String = "",
    val cookingTime: String = "",
    val category: String = "",
    val popularity: Int = 0
)




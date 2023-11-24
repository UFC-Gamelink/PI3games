package com.gamelink.gamelinkapp.service.model

data class GameModel(
    val id: Int,
    val title: String,
    val logo: Int,
    private val categories: List<String>,
    var selected: Boolean = false
) {
    val categoriesString: String
        get() = categories.joinToString(", ")
}
package com.example.composeshoppingchecklist.ui.model.model

data class ShoppingListUi(
    val id: Long,
    val shoppingListName: String,
    val shoppingListTimestamp: Long,
    val isArchived: Boolean
)
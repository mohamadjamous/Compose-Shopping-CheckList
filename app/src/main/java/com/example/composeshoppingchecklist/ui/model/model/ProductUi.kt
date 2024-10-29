package com.example.composeshoppingchecklist.ui.model.model

data class ProductUi(
    val id: Long,
    val productName: String,
    val productQuantity: Long,
    val productTimestamp: Long,
    val shoppingListId: Long
)
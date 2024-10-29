package com.example.composeshoppingchecklist.ui.model.state

sealed class ShoppingListState {
    object CURRENT : ShoppingListState()
    object ARCHIVED : ShoppingListState()
}